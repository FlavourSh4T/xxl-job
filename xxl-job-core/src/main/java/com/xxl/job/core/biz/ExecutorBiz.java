package com.xxl.job.core.biz;

import com.xxl.job.core.biz.model.*;

/**
 * Created by xuxueli on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat
     * @return
     */
    public ReturnT<String> beat();

    /**
     * idle beat
     *
     */
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run
     */
    public ReturnT<String> run(TriggerParam triggerParam);

    /**
     * kill
     */
    public ReturnT<String> kill(KillParam killParam);

    /**
     * log
     */
    public ReturnT<LogResult> log(LogParam logParam);

    /**
     * idle beat
     *
     * @deprecated Only for old version
     */
    @Deprecated
    default ReturnT<String> idleBeat(int jobId) {
        return ReturnT.SUCCESS;
    }

    /**
     * kill
     *
     * @deprecated Only for old version
     */
    default ReturnT<String> kill(int jobId) {
        return ReturnT.SUCCESS;
    }

    /**
     * log
     *
     * @deprecated Only for old version
     */
    @Deprecated
    default ReturnT<LogResult> log(long logDateTim, long logId, int fromLineNum) {
        return new ReturnT<>(new LogResult());
    }

}
