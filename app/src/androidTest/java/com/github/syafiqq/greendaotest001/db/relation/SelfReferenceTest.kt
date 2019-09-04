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
class SelfReferenceTest {
    private var session: DaoSession? = null
    private var db: Database? = null
    private var helper: DaoMaster.DevOpenHelper? = null
    private var context: Context? = null
    private var dao1: UserDao? = null
    private var entity000: User? = null
    private var entity010: User? = null
    private var entity020: User? = null
    private var entity110: User? = null
    private var entity120: User? = null
    private var entity210: User? = null
    private var entity220: User? = null
    private var entities0: MutableList<User>? = null
    private var entities1: MutableList<User>? = null
    private var entities2a: MutableList<User>? = null
    private var entities2b: MutableList<User>? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao1 = session?.userDao

        entity000 = User(null, "000", "active", null)
        entity010 = User(null, "010", "active", null)
        entity020 = User(null, "020", "active", null)
        entity110 = User(null, "110", "active", null)
        entity120 = User(null, "120", "active", null)
        entity210 = User(null, "210", "active", null)
        entity220 = User(null, "220", "active", null)
        entities0 = mutableListOf(entity000!!)
        entities1 = mutableListOf(entity010!!, entity020!!)
        entities2a = mutableListOf(entity110!!, entity120!!)
        entities2b = mutableListOf(entity210!!, entity220!!)

        assertThat(entity000, `is`(notNullValue()))
        assertThat(entity010, `is`(notNullValue()))
        assertThat(entity020, `is`(notNullValue()))
        assertThat(entity110, `is`(notNullValue()))
        assertThat(entity120, `is`(notNullValue()))
        assertThat(entity210, `is`(notNullValue()))
        assertThat(entity220, `is`(notNullValue()))
        assertThat(entities0, `is`(notNullValue()))
        assertThat(entities1, `is`(notNullValue()))
        assertThat(entities2a, `is`(notNullValue()))
        assertThat(entities2b, `is`(notNullValue()))
        assertThat(context, `is`(notNullValue()))
        assertThat(helper, `is`(notNullValue()))
        assertThat(db, `is`(notNullValue()))
        assertThat(session, `is`(notNullValue()))
        assertThat(dao1, `is`(notNullValue()))
    }

    @After
    fun tearDown() {
        dao1?.deleteAll()
        session?.clear()
        db?.close()
        helper?.close()
    }

    @Test
    fun it_should_insert_all_level() {
        entities0?.let { dao1?.insertInTx(it) }
        entities1?.let { dao1?.insertInTx(it) }
        entities2a?.let { dao1?.insertInTx(it) }
        entities2b?.let { dao1?.insertInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(7L)))
    }

    @Test
    fun it_should_attach_level0_and_level1() {
        entities0?.let { dao1?.insertInTx(it) }
        entities1?.let { dao1?.insertInTx(it) }

        entities1?.forEach { it.parent = entity000 }
        entities1?.let { dao1?.saveInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(3L)))

        val entities = dao1?.loadAll()
        val actualEntities0 = entities?.first { it.id == entity000?.id }
        val actualEntities1 = entities?.filter { al -> entities1?.map(User::getId)?.contains(al.id) ?: false }

        assertThat(actualEntities0, `is`(notNullValue()))
        assertThat(actualEntities0?.children, `is`(notNullValue()))
        assertThat(actualEntities0?.children, `is`(equalTo(entities1)))
        assertThat(actualEntities0?.children, `is`(equalTo(actualEntities1)))

        actualEntities1?.forEach {
            assertThat(entities1?.first{e -> e.id == it.id}, `is`(notNullValue()))
            assertThat(it.parent, `is`(notNullValue()))
            assertThat(it.parentId, `is`(notNullValue()))
            assertThat(it.parent, `is`(allOf(equalTo(entity000), equalTo(actualEntities0))))
        }
    }

    @Test
    fun it_should_attach_level1_and_level2() {
        entities1?.let { dao1?.insertInTx(it) }
        entities2a?.let { dao1?.insertInTx(it) }
        entities2b?.let { dao1?.insertInTx(it) }

        entities2a?.forEach { it.parent = entity010 }
        entities2b?.forEach { it.parent = entity020 }
        entities2a?.let { dao1?.saveInTx(it) }
        entities2b?.let { dao1?.saveInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(6L)))

        val entities = dao1?.loadAll()
        val actualEntities1 = entities?.filter { al -> entities1?.map(User::getId)?.contains(al.id) ?: false }
        val actualEntities2a = entities?.filter { al -> entities2a?.map(User::getId)?.contains(al.id) ?: false }
        val actualEntities2b = entities?.filter { al -> entities2b?.map(User::getId)?.contains(al.id) ?: false }

        actualEntities1?.forEach {
            val entity = if(it.name == "010") entities2a else entities2b
            val actualEntity = if(it.name == "010") actualEntities2a else actualEntities2b
            assertThat(it, `is`(notNullValue()))
            assertThat(it?.children, `is`(notNullValue()))
            assertThat(it?.children, `is`(equalTo(entity)))
            assertThat(it?.children, `is`(equalTo(actualEntity)))
        }

        mapOf(0 to actualEntities2a, 1 to actualEntities2b).forEach { i, l ->
            l?.forEach {
                val entity = if(i == 0) entity010 else entity020
                val entities = if(i == 0) entities2a else entities2b
                val actualEntity = if(i == 0) actualEntities1?.first { k -> k.id == entity010?.id } else actualEntities1?.first { k -> k.id == entity020?.id }
                assertThat(entities?.first{e -> e.id == it.id}, `is`(notNullValue()))
                assertThat(it.parent, `is`(notNullValue()))
                assertThat(it.parentId, `is`(notNullValue()))
                assertThat(it.parent, `is`(allOf(equalTo(entity), equalTo(actualEntity))))
            }
        }
    }
}