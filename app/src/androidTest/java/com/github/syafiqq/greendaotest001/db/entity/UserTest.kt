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
    private var conname: Context? = null
    private var dao: UserDao? = null
    private var user : User? = null

    @Before
    fun setUp() {
        conname = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(conname, "db-test")
        db = helper?.writableDb
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.userDao
        user = User().apply {
            this.name = "This is name"
            this.status = "active"
        }

        Assert.assertThat(user, IsNot(IsNull()))
        Assert.assertThat(conname, IsNot(IsNull()))
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
    fun it_should_select_all_users() {
        val users = dao?.loadAll()

        Assert.assertThat(users, IsInstanceOf(MutableList::class.java))
    }

    @Test
    fun it_should_insert_user() {
        user?.let { dao?.insert(it) }

        val users = dao?.loadAll()

        Assert.assertThat(users, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(users?.size, IsEqual(1))
        val actualUser = users?.first()
        Assert.assertThat(actualUser?.id, IsEqual(user?.id))
        Assert.assertThat(actualUser?.id, IsEqual(1L))
        Assert.assertThat(actualUser?.name, IsEqual(user?.name))
        Assert.assertThat(actualUser?.status, IsEqual(user?.status))
    }

    @Test
    fun it_should_delete_user() {
        user?.let { dao?.insert(it) }

        var users = dao?.loadAll()

        Assert.assertThat(users, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(users?.size, IsEqual(1))

        dao?.deleteByKey(user?.id)

        users = dao?.loadAll()

        Assert.assertThat(users, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(users?.size, IsEqual(0))
    }

    @Test
    fun it_should_update_name() {
        val newText = "New Text"
        Assert.assertThat(newText, IsNot(IsEqual(user?.name)))

        user?.let { dao?.insert(it) }

        var users = dao?.loadAll()

        Assert.assertThat(users, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(users?.size, IsEqual(1))
        Assert.assertThat(users?.first()?.name, IsEqual(user?.name))

        user?.name = newText
        dao?.update(user)

        users = dao?.loadAll()

        Assert.assertThat(users, IsInstanceOf(MutableList::class.java))
        Assert.assertThat(users?.size, IsEqual(1))
        Assert.assertThat(users?.first()?.name, IsEqual(newText))
    }
}