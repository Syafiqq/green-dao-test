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
class OneToManyTest {
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
    fun it_should_attached_all_via_update() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }
        entity1?.forEach {it.user = entity2}
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

    @Test
    fun it_should_attached_all_via_save() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }
        entity1?.forEach {it.user = entity2}
        entity1?.let { dao1?.saveInTx(it) }

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

    @Test
    fun it_should_attached_all_via_transaction() {
        db?.beginTransaction()
        try {
            entity1?.let { dao1?.insertInTx(it) }
            entity2?.let { dao2?.insert(it) }
            entity1?.forEach {it.user = entity2}
            entity1?.let { dao1?.saveInTx(it) }
            db?.setTransactionSuccessful()
        } catch (ex: Exception) {
        } finally {
            db?.endTransaction()
        }

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

    @Test
    fun it_should_not_insert_related_property() {
        entity1?.first()?.user = entity2
        entity1?.first()?.let { dao1?.insert(it) }

        assertThat(dao1?.count(), `is`(equalTo(1L)))
        assertThat(dao2?.count(), `is`(equalTo(0L)))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun it_should_throw_via_insert_again() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }
        entity1?.forEach {it.user = entity2}
        entity1?.let { dao1?.insertInTx(it) }
    }

    @Test
    fun it_should_update_list_after_insert() {
        val subEntity10 = entity1?.drop(0)?.take(2)
        subEntity10?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }
        subEntity10?.forEach {it.user = entity2}
        subEntity10?.let { dao1?.updateInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(2L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        subEntity10?.forEachIndexed { i, n -> assertThat(n.id, `is`(equalTo(i + 1L))) }

        val entities2 = dao2?.loadAll()
        val actualEntity2 = entities2?.first()
        assertThat(actualEntity2, `is`(notNullValue()))
        assertThat(actualEntity2?.notes, `is`(notNullValue()))
        assertThat(actualEntity2?.notes?.size, `is`(equalTo(2)))
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

        val subEntity11 = entity1?.drop(2)?.take(3)
        subEntity11?.let { dao1?.insertInTx(it) }
        subEntity11?.forEach {it.user = entity2}
        subEntity11?.let { dao1?.updateInTx(it) }

        val entities20 = dao2?.loadAll()
        val actualEntity20 = entities20?.first()
        assertThat(actualEntity20, `is`(notNullValue()))
        assertThat(actualEntity20?.notes, `is`(notNullValue()))
        assertThat(actualEntity20?.notes?.size, `is`(equalTo(2)))
        actualEntity20?.notes?.forEach {n ->
            assertThat(n?.user, `is`(notNullValue()))
            assertThat(n?.userId, `is`(notNullValue()))
            assertThat(n?.userId, `is`(equalTo(entity2?.id)))
        }

        actualEntity20?.resetNotes()

        assertThat(actualEntity20, `is`(notNullValue()))
        assertThat(actualEntity20?.notes, `is`(notNullValue()))
        assertThat(actualEntity20?.notes?.size, `is`(equalTo(5)))
        actualEntity20?.notes?.forEach {n ->
            assertThat(n?.user, `is`(notNullValue()))
            assertThat(n?.userId, `is`(notNullValue()))
            assertThat(n?.userId, `is`(equalTo(entity2?.id)))
        }
    }

    @Test
    fun it_should_update_list_after_switch() {
        val subEntity10 = entity1?.drop(0)?.take(2)
        subEntity10?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }
        subEntity10?.forEach {it.user = entity2}
        subEntity10?.let { dao1?.updateInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(2L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        subEntity10?.forEachIndexed { i, n -> assertThat(n.id, `is`(equalTo(i + 1L))) }

        val entities2 = dao2?.loadAll()
        val actualEntity2 = entities2?.first()
        assertThat(actualEntity2, `is`(notNullValue()))
        assertThat(actualEntity2?.notes, `is`(notNullValue()))
        assertThat(actualEntity2?.notes?.size, `is`(equalTo(2)))
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

        val subEntity11 = entity1?.drop(2)?.take(3)
        subEntity11?.let { dao1?.insertInTx(it) }

        subEntity10?.forEach {it.user = null}
        subEntity11?.forEach {it.user = entity2}
        subEntity11?.let { dao1?.updateInTx(it) }
        subEntity10?.let { dao1?.updateInTx(it) }

        val entities20 = dao2?.loadAll()
        val actualEntity20 = entities20?.first()
        assertThat(actualEntity20, `is`(notNullValue()))
        assertThat(actualEntity20?.notes, `is`(notNullValue()))
        assertThat(actualEntity20?.notes?.size, `is`(equalTo(2)))
        actualEntity20?.notes?.forEach {n ->
            assertThat(n?.user, `is`(nullValue()))
            assertThat(n?.userId, `is`(nullValue()))
        }

        actualEntity20?.resetNotes()

        assertThat(actualEntity20, `is`(notNullValue()))
        assertThat(actualEntity20?.notes, `is`(notNullValue()))
        assertThat(actualEntity20?.notes?.size, `is`(equalTo(3)))
        actualEntity20?.notes?.forEach {n ->
            assertThat(n?.user, `is`(notNullValue()))
            assertThat(n?.userId, `is`(notNullValue()))
            assertThat(n?.userId, `is`(equalTo(entity2?.id)))
        }
    }

    @Test
    fun it_should_raised_inconsistency() {
        entity1?.let { dao1?.insertInTx(it) }
        entity2?.let { dao2?.insert(it) }
        entity1?.forEach {it.user = entity2}
        entity1?.let { dao1?.saveInTx(it) }

        assertThat(dao1?.count(), `is`(equalTo(5L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        entity2?.let {
            dao2?.delete(it)
            dao2?.detach(it)
        }
        dao1?.detachAll()
        dao2?.detachAll()

        assertThat(dao1?.count(), `is`(equalTo(5L)))
        assertThat(dao2?.count(), `is`(equalTo(0L)))
        assertThat(dao2?.loadAll()?.size, `is`(equalTo(0)))

        dao1?.loadAll()?.forEach {
            assertThat(it?.user, `is`(nullValue()))
            assertThat(it?.userId, `is`(equalTo(1L)))
        }
    }
}