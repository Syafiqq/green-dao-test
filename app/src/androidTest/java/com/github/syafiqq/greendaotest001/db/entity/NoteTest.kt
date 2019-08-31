package com.github.syafiqq.greendaotest001.db.entity

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.syafiqq.greendaotest001.entity.DaoMaster
import com.github.syafiqq.greendaotest001.entity.DaoSession
import com.github.syafiqq.greendaotest001.entity.Note
import com.github.syafiqq.greendaotest001.entity.NoteDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
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

        assertThat(entity, IsNot(IsNull()))
        assertThat(context, IsNot(IsNull()))
        assertThat(helper, IsNot(IsNull()))
        assertThat(db, IsNot(IsNull()))
        assertThat(session, IsNot(IsNull()))
        assertThat(dao, IsNot(IsNull()))
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

        assertThat(entities, IsInstanceOf(MutableList::class.java))
        assertThat(entities?.size, IsEqual(0))
    }

    @Test
    fun it_should_insert_note() {
        entity?.let { dao?.insert(it) }

        val entities = dao?.loadAll()

        assertThat(entities, IsInstanceOf(MutableList::class.java))
        assertThat(entities?.size, IsEqual(1))
        val actualEntity = entities?.first()
        assertThat(actualEntity?.id, IsEqual(entity?.id))
        assertThat(actualEntity?.id, IsEqual(1L))
        assertThat(actualEntity?.text, IsEqual(entity?.text))
        assertThat(actualEntity?.date, IsEqual(entity?.date))
    }

    @Test
    fun it_should_delete_note() {
        entity?.let { dao?.insert(it) }

        var entities = dao?.loadAll()

        assertThat(entities, IsInstanceOf(MutableList::class.java))
        assertThat(entities?.size, IsEqual(1))

        dao?.deleteByKey(entity?.id)

        entities = dao?.loadAll()

        assertThat(entities, IsInstanceOf(MutableList::class.java))
        assertThat(entities?.size, IsEqual(0))
    }

    @Test
    fun it_should_update_name() {
        val newText = "New Text"
        assertThat(newText, IsNot(IsEqual(entity?.text)))

        entity?.let { dao?.insert(it) }

        var entities = dao?.loadAll()

        assertThat(entities, IsInstanceOf(MutableList::class.java))
        assertThat(entities?.size, IsEqual(1))
        assertThat(entities?.first()?.text, IsEqual(entity?.text))

        entity?.text = newText
        dao?.update(entity)

        entities = dao?.loadAll()

        assertThat(entities, IsInstanceOf(MutableList::class.java))
        assertThat(entities?.size, IsEqual(1))
        assertThat(entities?.first()?.text, IsEqual(newText))
    }
}