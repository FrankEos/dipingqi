
package org.harvest.web.constant;


/**
 * 控制节点常量配置
 * 
 * @author lance.xue
 */
public class ServerContants {

    /**
     * JMS消息中间件连接工厂名称
     */
    public static final String JMS_CONN_FACTORY_NAME = "ConnectionFactory";

    /**
     * JMS消息中间件 任务消息通知 消息订阅主题
     */
    public static final String TASK_MSG_NOTIFY = "TASK_MSG_NOTIFY";

    /**
     * JMS消息中间件 任务分配队列 消息订阅主题
     */
    public static final String TASK_QUE_NOTIFY = "TASK_QUE_NOTIFY";

    /**
     * JMS消息中间件 任务结果回报 消息订阅主题
     */
    public static final String TASK_RST_NOTIFY = "TASK_RST_NOTIFY";

    /**
     * 控制节点消息订阅主题
     */
    public static final String CONTROL_NODE_TOPIC = "CONTROL_NODE_TOPIC";

    /**
     * 处理节点消息订阅主题
     */
    public static final String REVIEW_NODE_TOPIC = "REVIEW_NODE_TOPIC";

    /**
     * 处理节点每次任务数量
     */
    public static final int TASK_LIST_COUNT = 10;

    // TODO 这里的实现和hld不一样，需要修改hld文档
    // 实现 taskType int 表示检测类型 0表示频道  1表示节目组 2表示节目 3表示站点 4表示检测所有节目
    /**
     * <p>0表示频道</p>
     * <p>[typeIds:0表示检测所有节目；1表示电影；2表示电视剧；3表示综艺；4表示动漫；5表示记录片；6表示公开课 ]</p>
     * <p>如果pgmAmount>0 则表示每个频道检测多少个节目</p>
     */
    public static final int TASK_TYPE_CHANNEL = 0;

    /**
     * <p>1表示节目组ids</p>
     * <p>例如：[typeIds: 8096484,8096485,8096469,8096466,8096465,7032033,8096460,8096461,8096455,7032214]</p>
     */
    public static final int TASK_TYPE_GRP = 1;

    /**
     * <p>2表示节目ids</p>
     * <p>例如[typeIds:8296393,8296392,8296389,8296388,8296386,8296384,8296383]</p>
     */
    public static final int TASK_TYPE_PGM = 2;

    /**
     * <p>3表示站点code</p>
     * <p>例如 [typeIds:1,2,3,4,5]</p>
     * <p>如果pgmAmount>0 则表示每个站点检测多少个节目</p>
     */
    public static final int TASK_TYPE_WEBSITE = 3;

    /**
     * <p>4表示检测所有节目</p>
     * <p>如果pgmAmount>0 则表示检测多少个节目</p>
     */
    public static final int TASK_TYPE_ALL = 4;
    

    /**
     * <p>5表示检测失效节目</p>
     */
    public static final int TASK_TYPE_FAILED = 5;
    
    /**
     * <p>6表示检测最新采集节目</p>
     */
    public static final int TASK_TYPE_NEW = 6;
    
    /**
     * <p>7表示检测失败流水节目</p>
     */
    public static final int TASK_TYPE_FAILED_SEQ = 7;

    /**
     * <p>8表示执行sql语句检测</p>
     */
    public static final int TASK_TYPE_SQL = 8;
    
    /**
     * <p>9表示执行未检测数据的检测</p>
     */
    public static final int TASK_TYPE_UNCHECKED = 9;
    
    /**
     * <p>10表示电视剧检测类型</p>
     */
    public static final int TASK_TYPE_TVSET = 10;
    

    // 消息类型，1命令、2取消命令、3心跳、4反馈、5配置
    /**
     * 1命令
     */
    public static final int MSG_TASK_ORDER = 1;

    /**
     * 2取消命令
     */
    public static final int MSG_CANCEL_ORDER = 2;

    /**
     * 3心跳
     */
    public static final int MSG_HEART_BEAT = 3;

    /**
     * 4反馈
     */
    public static final int MSG_FEED_BACK = 4;

    /**
     * 5配置
     */
    public static final int MSG_CONFIGURE = 5;

    // 任务执行的方式，0表示即时任务，1表示周期任务，2表示定时任务
    /**
     * 0表示即时任务
     */
    public static final int TASK_METHOD_REALTIME = 0;

    /**
     * 1表示周期任务
     */
    public static final int TASK_METHOD_INTERVAL = 1;

    /**
     * 2表示定时任务
     */
    public static final int TASK_METHOD_FIXTIME = 2;
    
    //pgmOrder：int型；表示检测分类的排序，为0表示默认排序
    //排序类型1表示从新到旧，2表示从旧到新；
    //排序类型3表示评分从高到低，4表示评分从低到高；
    //排序类型5表示点击数从高到低，6表示点击数从低到高；
    /**
     * 0表示默认排序
     */
    public static final int ORDER_BY_DEFAULT = 0;
    
    /**
     * 1表示从新到旧
     */
    public static final int ORDER_BY_NEW2OLD = 1;
    
    /**
     * 2表示从旧到新
     */
    public static final int ORDER_BY_OLD2NEW = 2;
    
    /**
     * 3表示评分从高到低
     */
    public static final int ORDER_BY_SCORE_H2L = 3;
    
    /**
     * 4表示评分从低到高
     */
    public static final int ORDER_BY_SCORE_L2H = 4;
    
    /**
     * 5表示点击数从高到低
     */
    public static final int ORDER_BY_COUNT_H2L = 5;
    
    /**
     * 6表示点击数从低到高
     */
    public static final int ORDER_BY_COUNT_L2H = 6;
    
    /**
     * 7表示随机抽取数据进行检测
     */
    public static final int ORDER_BY_RANDOM = 7;
    
    
    /**
     * 查询分页数量
     */
    public static final int REVIEW_PAGE_SIZE = 500;



}
