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
import org.hamcrest.CoreMatchers.*
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

        Assert.assertThat(entity, `is`(notNullValue()))
        Assert.assertThat(context, `is`(notNullValue()))
        Assert.assertThat(helper, `is`(notNullValue()))
        Assert.assertThat(db, `is`(notNullValue()))
        Assert.assertThat(session, `is`(notNullValue()))
        Assert.assertThat(dao, `is`(notNullValue()))
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

        Assert.assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        Assert.assertThat(entities?.size, `is`(equalTo(0)))
    }

    @Test
    fun it_should_insert_user() {
        entity?.let { dao?.insert(it) }

        val entities = dao?.loadAll()

        Assert.assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        Assert.assertThat(entities?.size, `is`(equalTo(1)))
        val actualEntity = entities?.first()
        Assert.assertThat(actualEntity?.id, `is`(equalTo(entity?.id)))
        Assert.assertThat(actualEntity?.id, `is`(equalTo(1L)))
        Assert.assertThat(actualEntity?.name, `is`(equalTo(entity?.name)))
        Assert.assertThat(actualEntity?.status, `is`(equalTo(entity?.status)))
    }

    @Test
    fun it_should_delete_user() {
        entity?.let { dao?.insert(it) }

        var entities = dao?.loadAll()

        Assert.assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        Assert.assertThat(entities?.size, `is`(equalTo(1)))

        dao?.deleteByKey(entity?.id)

        entities = dao?.loadAll()

        Assert.assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        Assert.assertThat(entities?.size, `is`(equalTo(0)))
    }

    @Test
    fun it_should_update_name() {
        val newText = "New Text"
        Assert.assertThat(newText, `is`(not(equalTo(entity?.name))))

        entity?.let { dao?.insert(it) }

        var entities = dao?.loadAll()

        Assert.assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        Assert.assertThat(entities?.size, `is`(equalTo(1)))
        Assert.assertThat(entities?.first()?.name, `is`(equalTo(entity?.name)))

        entity?.name = newText
        dao?.update(entity)

        entities = dao?.loadAll()

        Assert.assertThat(entities, `is`(instanceOf(MutableList::class.java)))
        Assert.assertThat(entities?.size, `is`(equalTo(1)))
        Assert.assertThat(entities?.first()?.name, `is`(equalTo(newText)))
    }
}