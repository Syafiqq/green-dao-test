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
class OneToOneTest {
    private var session: DaoSession? = null
    private var db: Database? = null
    private var helper: DaoMaster.DevOpenHelper? = null
    private var context: Context? = null
    private var dao1: NoteDao? = null
    private var dao2: UserDao? = null
    private var entity1: Note? = null
    private var entity2: User? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao1 = session?.noteDao
        dao2 = session?.userDao
        entity1 = Note().apply {
            text = "This is text"
            date = Date()
        }
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
        entity1?.let { dao1?.insert(it) }
        entity2?.let { dao2?.insert(it) }

        assertThat(dao1?.count(), `is`(equalTo(1L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))
    }

    @Test
    fun it_should_attached_via_update() {
        entity1?.let { dao1?.insert(it) }
        entity2?.let { dao2?.insert(it) }
        entity1?.user = entity2
        entity1?.let { dao1?.update(it) }

        assertThat(dao1?.count(), `is`(equalTo(1L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        val entities1 = dao1?.loadAll()
        val actualEntity1 = entities1?.first()
        assertThat(actualEntity1, `is`(notNullValue()))
        assertThat(actualEntity1?.user, `is`(equalTo(entity2)))
        assertThat(actualEntity1?.userId, `is`(notNullValue()))
        assertThat(actualEntity1?.userId, `is`(equalTo(entity2?.id)))

        val entities2 = dao2?.loadAll()
        val actualEntity2 = entities2?.first()
        assertThat(actualEntity2, `is`(notNullValue()))
        assertThat(actualEntity2?.notes, `is`(notNullValue()))
        assertThat(actualEntity2?.notes?.size, `is`(equalTo(1)))
        actualEntity2?.notes?.forEach {n ->
            assertThat(n?.user, `is`(notNullValue()))
            assertThat(n?.userId, `is`(notNullValue()))
            assertThat(n?.userId, `is`(equalTo(entity2?.id)))
        }
    }

    @Test
    fun it_should_attached_via_save() {
        entity1?.let { dao1?.save(it) }
        entity2?.let { dao2?.insert(it) }
        entity1?.user = entity2
        entity1?.let { dao1?.save(it) }

        assertThat(dao1?.count(), `is`(equalTo(1L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        val entities1 = dao1?.loadAll()
        val actualEntity1 = entities1?.first()
        assertThat(actualEntity1, `is`(notNullValue()))
        assertThat(actualEntity1?.user, `is`(equalTo(entity2)))
        assertThat(actualEntity1?.userId, `is`(notNullValue()))
        assertThat(actualEntity1?.userId, `is`(equalTo(entity2?.id)))

        val entities2 = dao2?.loadAll()
        val actualEntity2 = entities2?.first()
        assertThat(actualEntity2, `is`(notNullValue()))
        assertThat(actualEntity2?.notes, `is`(notNullValue()))
        assertThat(actualEntity2?.notes?.size, `is`(equalTo(1)))
        actualEntity2?.notes?.forEach {n ->
            assertThat(n?.user, `is`(notNullValue()))
            assertThat(n?.userId, `is`(notNullValue()))
            assertThat(n?.userId, `is`(equalTo(entity2?.id)))
        }
    }

    @Test
    fun it_should_attached_via_transaction() {
        db?.beginTransaction()
        try {
            entity1?.let { dao1?.insert(it) }
            entity2?.let { dao2?.insert(it) }
            entity1?.user = entity2
            entity1?.let { dao1?.save(it) }
            db?.setTransactionSuccessful()
        } catch (ex: Exception) {
        } finally {
            db?.endTransaction()
        }

        assertThat(dao1?.count(), `is`(equalTo(1L)))
        assertThat(dao2?.count(), `is`(equalTo(1L)))

        val entities1 = dao1?.loadAll()
        val actualEntity1 = entities1?.first()
        assertThat(actualEntity1, `is`(notNullValue()))
        assertThat(actualEntity1?.user, `is`(equalTo(entity2)))
        assertThat(actualEntity1?.userId, `is`(notNullValue()))
        assertThat(actualEntity1?.userId, `is`(equalTo(entity2?.id)))

        val entities2 = dao2?.loadAll()
        val actualEntity2 = entities2?.first()
        assertThat(actualEntity2, `is`(notNullValue()))
        assertThat(actualEntity2?.notes, `is`(notNullValue()))
        assertThat(actualEntity2?.notes?.size, `is`(equalTo(1)))
        actualEntity2?.notes?.forEach {n ->
            assertThat(n?.user, `is`(notNullValue()))
            assertThat(n?.userId, `is`(notNullValue()))
            assertThat(n?.userId, `is`(equalTo(entity2?.id)))
        }
    }

    @Test
    fun it_should_not_insert_related_property() {
        entity1?.user = entity2
        entity1?.let { dao1?.insert(it) }

        assertThat(dao1?.count(), `is`(equalTo(1L)))
        assertThat(dao2?.count(), `is`(equalTo(0L)))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun it_should_throw_via_insert_again() {
        entity1?.let { dao1?.insert(it) }
        entity2?.let { dao2?.insert(it) }
        entity1?.user = entity2
        entity1?.let { dao1?.insert(it) }
    }
}