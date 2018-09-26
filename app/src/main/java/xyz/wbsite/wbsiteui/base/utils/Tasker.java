package xyz.wbsite.wbsiteui.base.utils;

import android.os.AsyncTask;

/**
 * 任务执行器
 */
public class Tasker {

    /**
     * 执行任务
     * @param task
     */
    public void exec(Task task) {
        task.execute();
    }

    public abstract class Task extends AsyncTask<Void, String, Boolean> {

        /**
         * 异步运行任务
         *
         * @return 任务结果 true:达到预期目的，false:未达到预期目的
         */
        abstract boolean run();

        /**
         * 任务处理完毕后执行
         *
         * @param result
         */
        void post(boolean result) {
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                return run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            post(result == null ? false : result);
        }
    }
}
