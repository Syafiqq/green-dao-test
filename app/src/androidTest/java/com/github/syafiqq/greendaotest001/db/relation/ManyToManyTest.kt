package com.github.syafiqq.greendaotest001.db.relation

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.syafiqq.greendaotest001.entity.*
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ManyToManyTest {
    private var session: DaoSession? = null
    private var db: Database? = null
    private var helper: DaoMaster.DevOpenHelper? = null
    private var context: Context? = null
    private var dao1: RoleDao? = null
    private var dao2: UserDao? = null
    private var dao3: UserRoleDao? = null
    private var entity1: MutableList<Role>? = null
    private var entity2: MutableList<User>? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao1 = session?.roleDao
        dao2 = session?.userDao
        dao3 = session?.userRoleDao
        entity1 = mutableListOf(
            Role().apply {
                name = "This is text 1"
            },
            Role().apply {
                name = "This is text 2"
            },
            Role().apply {
                name = "This is text 3"
            },
            Role().apply {
                name = "This is text 4"
            },
            Role().apply {
                name = "This is text 5"
            }
        )
        entity2 = mutableListOf(
            User().apply {
                name = "This is text 1"
                status = "active"
            },
            User().apply {
                name = "This is text 2"
                status = "active"
            },
            User().apply {
                name = "This is text 3"
                status = "active"
            },
            User().apply {
                name = "This is text 4"
                status = "active"
            },
            User().apply {
                name = "This is text 5"
                status = "active"
            }
        )

        assertThat(entity1, `is`(notNullValue()))
        assertThat(entity2, `is`(notNullValue()))
        assertThat(context, `is`(notNullValue()))
        assertThat(helper, `is`(notNullValue()))
        assertThat(db, `is`(notNullValue()))
        assertThat(session, `is`(notNullValue()))
        assertThat(dao1, `is`(notNullValue()))
        assertThat(dao2, `is`(notNullValue()))
    }

    @After
    fun tearDown() {
        dao3?.deleteAll()
        dao2?.deleteAll()
        dao1?.deleteAll()
        session?.clear()
        db?.close()
        helper?.close()
    }

    @Test
    fun it_should_insert_both() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insertInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(5L)))
        assertThat(dao2?.count(), `is`(equalTo(5L)))
        assertThat(dao3?.count(), `is`(equalTo(0L)))

        entity1?.forEachIndexed { i, n -> assertThat(n.id, `is`(equalTo(i + 1L))) }
        entity2?.forEachIndexed { i, n -> assertThat(n.id, `is`(equalTo(i + 1L))) }

        dao1?.loadAll()?.forEach {
            assertThat(it.users, `is`(notNullValue()))
            assertThat(it.users.size, `is`(equalTo(0)))
        }
        dao2?.loadAll()?.forEach {
            assertThat(it.roles, `is`(notNullValue()))
            assertThat(it.roles.size, `is`(equalTo(0)))
        }
    }

    @Test
    fun it_should_attached_all_via_insert() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insertInTx(it) }
        val entity3 = entity1?.flatMap { r ->
            entity2?.map { u ->
                UserRole().apply {
                    user = u
                    role = r
                }
            } ?: listOf()
        }
        entity3?.let { dao3?.insertInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(5L)))
        assertThat(dao2?.count(), `is`(equalTo(5L)))
        assertThat(dao3?.count(), `is`(equalTo(25L)))

        dao1?.loadAll()?.forEach {
            assertThat(it.users, `is`(notNullValue()))
            assertThat(it.users.size, `is`(equalTo(5)))
        }

        dao2?.loadAll()?.forEach {
            assertThat(it.roles, `is`(notNullValue()))
            assertThat(it.roles.size, `is`(equalTo(5)))
        }
    }
}