package com.github.syafiqq.greendaotest001.db.entity

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.syafiqq.greendaotest001.entity.DaoMaster
import com.github.syafiqq.greendaotest001.entity.DaoSession
import com.github.syafiqq.greendaotest001.entity.Note
import com.github.syafiqq.greendaotest001.entity.NoteDao
import org.greenrobot.greendao.DaoException
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
class NoteTest {
    private var session: DaoSession? = null
    private var db: Database? = null
    private var helper: DaoMaster.DevOpenHelper? = null
    private var context: Context? = null
    private var dao: NoteDao? = null
    private var entity : Note? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao
        entity = Note().apply {
            this.text = "This is text"
            this.date = Date()
        }

        assertThat(entity, `is`(notNullValue()))
        assertThat(context, `is`(notNullValue()))
        assertThat(helper, `is`(notNullValue()))
        assertThat(db, `is`(notNullValue()))
        assertThat(session, `is`(notNullValue()))
        assertThat(dao, `is`(notNullValue()))
    }

    @After
    fun tearDown() {
        dao?.deleteAll()
        session?.clear()
        db?.close()
        helper?.close()
    }

    @Test
    fun it_should_select_all_entities() {
        val entities = dao?.loadAll()

        assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        assertThat(entities?.size, `is`(equalTo(0)))
    }

    @Test(expected = DaoException::class)
    fun it_should_return_getter() {
        assertThat(entity?.id, `is`(nullValue()))
        assertThat(entity?.text, `is`(notNullValue()))
        assertThat(entity?.date, `is`(notNullValue()))
        assertThat(entity?.userId, `is`(nullValue()))
        // Todo : Check later
        assertThat(entity?.user, `is`(nullValue()))
    }

    @Test
    fun it_should_insert_note() {
        entity?.let { dao?.insert(it) }

        val entities = dao?.loadAll()

        assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        assertThat(entities?.size, `is`(equalTo(1)))
        entities?.forEach {
            assertThat(it?.id, `is`(equalTo(entity?.id)))
            assertThat(it?.id, `is`(equalTo(1L)))
            assertThat(it?.text, `is`(equalTo(entity?.text)))
            assertThat(it?.date, `is`(equalTo(entity?.date)))
        }
    }

    @Test
    fun it_should_delete_note() {
        entity?.let { dao?.insert(it) }

        var entities = dao?.loadAll()

        assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        assertThat(entities?.size, `is`(equalTo(1)))

        dao?.deleteByKey(entity?.id)

        entities = dao?.loadAll()

        assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        assertThat(entities?.size, `is`(equalTo(0)))
    }

    @Test
    fun it_should_update_name() {
        val newText = "New Text"
        assertThat(newText, `is`(not(equalTo(entity?.text))))

        entity?.let { dao?.insert(it) }

        var entities = dao?.loadAll()

        assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        assertThat(entities?.size, `is`(equalTo(1)))
        assertThat(entities?.first()?.text, `is`(equalTo(entity?.text)))

        entity?.text = newText
        dao?.update(entity)

        entities = dao?.loadAll()

        assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        assertThat(entities?.size, `is`(equalTo(1)))
        assertThat(entities?.first()?.text, `is`(equalTo(newText)))
    }

    @Test
    fun it_should_persist_cache_after_delete() {
        entity?.let { dao?.insert(it) }
        dao?.detach(entity)

        var entities = dao?.loadAll()

        assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        assertThat(entities?.size, `is`(equalTo(1)))
        assertThat(entities?.first(), `is`(equalTo(entity)))

        dao?.delete(entity)
        assertThat(entities?.size, `is`(equalTo(1)))
        assertThat(entities?.first(), `is`(equalTo(entity)))

        entities = dao?.loadAll()
        assertThat(entities?.size, `is`(equalTo(0)))
    }
}