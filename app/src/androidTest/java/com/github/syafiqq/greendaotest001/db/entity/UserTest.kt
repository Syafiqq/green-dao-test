package com.github.syafiqq.greendaotest001.db.entity

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.syafiqq.greendaotest001.entity.DaoMaster
import com.github.syafiqq.greendaotest001.entity.DaoSession
import com.github.syafiqq.greendaotest001.entity.User
import com.github.syafiqq.greendaotest001.entity.UserDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserTest {
    private var session: DaoSession? = null
    private var db: Database? = null
    private var helper: DaoMaster.DevOpenHelper? = null
    private var context: Context? = null
    private var dao: UserDao? = null
    private var entity : User? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.userDao
        entity = User().apply {
            this.name = "This is name"
            this.status = "active"
        }

        Assert.assertThat(entity, IsNot(IsNull()))
        Assert.assertThat(context, IsNot(IsNull()))
        Assert.assertThat(helper, IsNot(IsNull()))
        Assert.assertThat(db, IsNot(IsNull()))
        Assert.assertThat(session, IsNot(IsNull()))
        Assert.assertThat(dao, IsNot(IsNull()))
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

        Assert.assertThat(entities, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(entities?.size, IsEqual(0))
    }

    @Test
    fun it_should_insert_user() {
        entity?.let { dao?.insert(it) }

        val entities = dao?.loadAll()

        Assert.assertThat(entities, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(entities?.size, IsEqual(1))
        val actualEntity = entities?.first()
        Assert.assertThat(actualEntity?.id, IsEqual(entity?.id))
        Assert.assertThat(actualEntity?.id, IsEqual(1L))
        Assert.assertThat(actualEntity?.name, IsEqual(entity?.name))
        Assert.assertThat(actualEntity?.status, IsEqual(entity?.status))
    }

    @Test
    fun it_should_delete_user() {
        entity?.let { dao?.insert(it) }

        var entities = dao?.loadAll()

        Assert.assertThat(entities, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(entities?.size, IsEqual(1))

        dao?.deleteByKey(entity?.id)

        entities = dao?.loadAll()

        Assert.assertThat(entities, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(entities?.size, IsEqual(0))
    }

    @Test
    fun it_should_update_name() {
        val newText = "New Text"
        Assert.assertThat(newText, IsNot(IsEqual(entity?.name)))

        entity?.let { dao?.insert(it) }

        var entities = dao?.loadAll()

        Assert.assertThat(entities, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(entities?.size, IsEqual(1))
        Assert.assertThat(entities?.first()?.name, IsEqual(entity?.name))

        entity?.name = newText
        dao?.update(entity)

        entities = dao?.loadAll()

        Assert.assertThat(entities, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(entities?.size, IsEqual(1))
        Assert.assertThat(entities?.first()?.name, IsEqual(newText))
    }
}