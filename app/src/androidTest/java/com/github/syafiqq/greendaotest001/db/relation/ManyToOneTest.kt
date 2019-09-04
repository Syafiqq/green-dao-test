package com.github.syafiqq.greendaotest001.db.relation


import android.content.Context
import android.database.sqlite.SQLiteConstraintException
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
import java.util.*

@RunWith(AndroidJUnit4::class)
class ManyToOneTest {
    private var session: DaoSession? = null
    private var db: Database? = null
    private var helper: DaoMaster.DevOpenHelper? = null
    private var context: Context? = null
    private var dao1: NoteDao? = null
    private var dao2: UserDao? = null
    private var entity1: MutableList<Note>? = null
    private var entity2: User? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao1 = session?.noteDao
        dao2 = session?.userDao
        entity1 = mutableListOf(
            Note().apply {
                text = "This is text 1"
                date = Date()
            },
            Note().apply {
                text = "This is text 2"
                date = Date()
            },
            Note().apply {
                text = "This is text 3"
                date = Date()
            },
            Note().apply {
                text = "This is text 4"
                date = Date()
            },
            Note().apply {
                text = "This is text 5"
                date = Date()
            }
        )
        entity2 = User().apply {
            name = "This is test"
            status = "active"
        }

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
        dao1?.deleteAll()
        dao2?.deleteAll()
        session?.clear()
        db?.close()
        helper?.close()
    }

    @Test
    fun it_should_insert_both() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }

        assertThat(dao1?.count(), `is`(equalTo(5L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        entity1?.forEachIndexed { i, n -> assertThat(n.id, `is`(equalTo(i + 1L))) }
    }

    @Test
    fun it_should_not_attached_all_via_update() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }
        entity2?.notes = entity1
        entity1?.let { dao1?.updateInTx(it) }
        entity2?.let { dao2?.updateInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(5L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        entity1?.forEachIndexed { i, n -> assertThat(n.id, `is`(equalTo(i + 1L))) }

        val entities2 = dao2?.loadAll()
        val actualEntity2 = entities2?.first()
        assertThat(actualEntity2, `is`(notNullValue()))
        assertThat(actualEntity2?.notes, `is`(notNullValue()))
        assertThat(actualEntity2?.notes?.size, `is`(equalTo(5)))
        actualEntity2?.notes?.forEach {n ->
            assertThat(n?.user, `is`(nullValue()))
            assertThat(n?.userId, `is`(nullValue()))
            assertThat(n?.userId, `is`(not(equalTo(entity2?.id))))
        }

        val entities1 = dao1?.loadAll()
        val actualEntity1 = entities1?.first()
        assertThat(actualEntity1, `is`(notNullValue()))
        assertThat(actualEntity1?.user, `is`(not(equalTo(entity2))))
        assertThat(actualEntity1?.userId, `is`(nullValue()))
        assertThat(actualEntity1?.userId, `is`(not(equalTo(entity2?.id))))
    }

    @Test
    fun it_should_attached_due_to_extension_via_update() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }
        entity1?.let { entity2?.assignNotes(it) }
        entity1?.let { dao1?.updateInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(5L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        entity1?.forEachIndexed { i, n -> assertThat(n.id, `is`(equalTo(i + 1L))) }

        val entities2 = dao2?.loadAll()
        val actualEntity2 = entities2?.first()
        assertThat(actualEntity2, `is`(notNullValue()))
        assertThat(actualEntity2?.notes, `is`(notNullValue()))
        assertThat(actualEntity2?.notes?.size, `is`(equalTo(5)))
        actualEntity2?.notes?.forEach {n ->
            assertThat(n?.user, `is`(notNullValue()))
            assertThat(n?.userId, `is`(notNullValue()))
            assertThat(n?.userId, `is`(equalTo(entity2?.id)))
        }

        val entities1 = dao1?.loadAll()
        val actualEntity1 = entities1?.first()
        assertThat(actualEntity1, `is`(notNullValue()))
        assertThat(actualEntity1?.user, `is`(equalTo(entity2)))
        assertThat(actualEntity1?.userId, `is`(notNullValue()))
        assertThat(actualEntity1?.userId, `is`(equalTo(entity2?.id)))
    }
}

fun User.assignNotes(notes: Collection<Note> = listOf()) {
    notes.forEach { it.user = this }
}