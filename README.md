# 使用Android Room 處理存取手機的Database

<br>

Android Room是Android的套件之一, 他可以簡化不少方法, 方便大家使用資料庫, 雖然過去的SQLiteOpenHelper可以更自由的存取資料庫資料, 但Room簡化了更多的步驟, 減少不少開發上的困難

<br>

先來看看 Room 有哪些特點：
1. Room 把一些 SQLite 底層實作封裝起來讓我們能更方便存取資料庫，不需要再寫冗長的程式碼才能將 SQL 和 Kotlin程式資料類別轉換
2. Room支援編譯時期的 SQL 語法檢查，不需要等到執行後才能發現錯誤。
3. 容易整合且語法簡單需多，少掉很多囉唆的程式碼。
4. 支援LiveData / RxJava，可以使用觀察者模式來訂閱資料變更。

架構圖如以下, 取自於Save data in a local database using Room





<br>



### build.gradle

在dependencies 加入以下方法, 即可使用Room方法

```javascript
implementation "androidx.room:room-runtime:2.2.5"
annotationProcessor 'android.arch.persistence.room:compiler:1.1.1'
```

<br>

### TaskDao.java

DAO方法是定義所有資料庫的存取方法, 包含查詢和新刪修
而查詢方法可以利用@Query(SQL)實作

```java
@Dao
public interface TaskDao {
    @Query("SELECT * FROM Task")
    List<Task> getAllTask();

    @Query("delete from Task where taskName = :taskName")
    void deleteTask(String taskName);

    @Insert
    void insert(Task... tasks);

    @Update
    void update(Task... tasks);

    @Delete
    void delete(Task... tasks);

}

```

<br>

### Task.java  (Entity)

Entity即為實體資料, 用來製作欄位

```java
@Entity(tableName = "Task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String sort;
    private String taskName;
    private String startTime;
    private String endTime;
    private int spendTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(int spendTime) {
        this.spendTime = spendTime;
    }
```

<br>

### TaskDatabase.java(RoomDatabase)

資料庫本身的宣告方法, 包含版本管理, 建立均在此建立



```java
@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    private static final String DB_NAME = "TaskDatabase.db";
    private static volatile TaskDatabase instance;

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static TaskDatabase create(final Context context) {
        return Room.databaseBuilder(context, TaskDatabase.class, DB_NAME).build();
    }

    public abstract TaskDao getTaskDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {
    }
}
```

### 執行MainActivity.java (主專案)

在主專案即可執行Room方法, 即可存取Android Database資料, 要注意存取的時候必需要再Thread Handle, 目前AsyncTask是無法執行的



新增

```java
TaskDatabase.getInstance(MainActivity.this).getTaskDao().insert(task);
handle.sendEmptyMessage(INSERT_COMPLETE);

List<Task> taskList = TaskDatabase.getInstance(MainActivity.this).getTaskDao().getAllTask();
```

刪除一筆資料

```java
TaskDatabase.getInstance(MainActivity.this).getTaskDao().deleteTask(taskName);
```

