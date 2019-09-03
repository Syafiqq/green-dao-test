package com.github.syafiqq.greendaotest001.db.relation


import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.syafiqq.greendaotest001.entity.*
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
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

        assertThat(entity1, IsNot(IsNull()))
        assertThat(entity2, IsNot(IsNull()))
        assertThat(context, IsNot(IsNull()))
        assertThat(helper, IsNot(IsNull()))
        assertThat(db, IsNot(IsNull()))
        assertThat(session, IsNot(IsNull()))
        assertThat(dao1, IsNot(IsNull()))
        assertThat(dao2, IsNot(IsNull()))
    }

    @After
    fun tearDown() {
        dao1?.deleteAll()
        dao2?.deleteAll()
        session?.clear()
        db?.close()
        helper?.close()
    }
}