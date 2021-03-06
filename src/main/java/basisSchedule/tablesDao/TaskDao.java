package basisSchedule.tablesDao;

import basisSchedule.resultModel.Task;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import utils.Constants;
import utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class TaskDao {


    @Autowired ()
    @Qualifier( "schedulesqlSessionTemplate" )
    private SqlSessionTemplate sqlSessionSchedule;

    //查询任务表所有任务信息(分页)
    public List<Task> query(int page, int rows) {

        return sqlSessionSchedule.selectList(Constants.MAPPER_TASK + ".queryTask", new RowBounds((page - 1) * rows, rows));
    }

    //查询任务表所有任务信息(不分页)
    public List<Task> query() {
        return sqlSessionSchedule.selectList(Constants.MAPPER_TASK + ".queryTask");
    }

    //插入任务信息，成功返回插入数据的id
    public int insert(Task task) {
        try {
            sqlSessionSchedule.insert(Constants.MAPPER_TASK + ".insertTask", task);
            LogUtil.SuccessLogAdd(
                    Constants.LOG_INFO,
                    "任务 " + task.getTask_id(), "插入",true);
            return Constants.SUCCESS;
        } catch (DuplicateKeyException e) {
            LogUtil.ErrorLogAdd(
                    Constants.LOG_ERROR
                    , "任务 " + task.getTask_id(), "插入", "键值重复",true);
            return Constants.DUPLICATEKEYERROR;
        } catch (DataIntegrityViolationException e) {
            LogUtil.ErrorLogAdd(
                    Constants.LOG_ERROR
                    , "任务 " + task.getTask_id(), "插入", "违反完整性约束",true);
            return Constants.DATAVIOLATIONERROR;
        } catch (Exception e) {
            LogUtil.ErrorLogAdd(
                    Constants.LOG_ERROR
                    , "任务 " + task.getTask_id(), "插入", "其他原因",true);
            return Constants.UNKNOWNERROR;
        }
    }

    //删除任务信息
    public Map<Integer,Integer> delete(List<Integer> task_id) {
        Map<Integer,Integer> resultMap=new HashMap<Integer, Integer>();
        for (int i : task_id) {
            try {
                sqlSessionSchedule.delete(Constants.MAPPER_TASK + ".deleteTask", i);
                resultMap.put(i,Constants.SUCCESS);
                LogUtil.SuccessLogAdd(
                        Constants.LOG_INFO,
                        "任务 " + i, "删除",true);
            } catch (Exception e) {
                resultMap.put(i,Constants.FAIL);
                LogUtil.ErrorLogAdd(
                        Constants.LOG_ERROR,
                        "任务 " + i, "删除", "未知原因",true);
            }
        }
        return resultMap;
    }

}








