package com.accountbook.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.accountbook.data.local.dao.CategoryDao;
import com.accountbook.data.local.dao.RecordDao;
import com.accountbook.data.local.entity.CategoryEntity;
import com.accountbook.data.local.entity.RecordEntity;

import java.util.concurrent.Executors;

@Database(
        entities = {RecordEntity.class, CategoryEntity.class},
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP INDEX IF EXISTS index_categories_name");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_categories_name_type ON categories (name, type)");
        }
    };

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "accountbook-db"
                    )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                AppDatabase database = getDatabase(context);
                                populatePresetCategories(database.categoryDao());
                            });
                        }
                    })
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void populatePresetCategories(CategoryDao dao) {
        String[] expenseCategories = {"餐饮", "交通", "购物", "娱乐", "医疗", "教育", "住房", "通讯"};
        String[] incomeCategories = {"工资", "奖金", "投资", "兼职", "礼金"};
        
        for (int i = 0; i < expenseCategories.length; i++) {
            CategoryEntity entity = new CategoryEntity(expenseCategories[i], "EXPENSE", i, true);
            dao.insert(entity);
        }
        
        for (int i = 0; i < incomeCategories.length; i++) {
            CategoryEntity entity = new CategoryEntity(incomeCategories[i], "INCOME", i, true);
            dao.insert(entity);
        }
    }

    public abstract RecordDao recordDao();

    public abstract CategoryDao categoryDao();
}
