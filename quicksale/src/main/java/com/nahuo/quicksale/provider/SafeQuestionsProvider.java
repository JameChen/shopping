package com.nahuo.quicksale.provider;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.nahuo.bean.SafeBean;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SafeQuestion;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @description 安全问题
 * @created 2014-9-2 下午2:43:06
 * @author ZZB
 */
public class SafeQuestionsProvider {

    private static final SparseArray<String> mQuestions;

    static {
        mQuestions = new SparseArray<String>();
        mQuestions.put(1, "我最爱的人是?");
        mQuestions.put(2, "我的中学名称是?");
        mQuestions.put(3, "我的大学名称是?");
        mQuestions.put(4, "我爸爸的名字是?");
        mQuestions.put(5, "我妈妈的名字是?");
        mQuestions.put(6, "我爸爸的生日是?");
        mQuestions.put(7, "我妈妈的生日是?");
        mQuestions.put(8, "我妻子的名字是?");
        mQuestions.put(9, "我丈夫的名字是?");
        mQuestions.put(10, "我的出生地是?");
        mQuestions.put(11, "我的小学校名是?");
        mQuestions.put(12, "我的大学的名字是?");
        mQuestions.put(13, "我比较喜欢的球类运动是?");
        mQuestions.put(14, "我最喜欢的一首歌是?");
        mQuestions.put(15, "我最喜欢车的汽车品牌是?");
        mQuestions.put(16, "我最喜欢的一部电影名称是?");
        mQuestions.put(17, "我的幸运数字?");
        mQuestions.put(18, "我向往的旅游地是?");
        mQuestions.put(19, "我第一次购房的日期?");
        mQuestions.put(20, "我的宠物的名字是?");
        mQuestions.put(21, "我高考的总分是?");
        mQuestions.put(22, "我的工号是?");
        mQuestions.put(23, "我的结婚纪念日是?");
        mQuestions.put(24, "我第一辆车的车牌是?");
        
    }
    
    /**
     * @description 获取安全问题
     * @created 2014-9-2 下午3:14:35
     * @author ZZB
     */
    public static SparseArray<SafeQuestion[]> getQuestions(){
        SparseArray<SafeQuestion[]> questions = new SparseArray<SafeQuestion[]>();
        SparseIntArray indexes = genRandomIndex();
        int counter = 0;
        for(int i=0; i<3; i++){
            SafeQuestion[] qs = new SafeQuestion[6];
            for(int j=0; j<6; j++){
                int index = indexes.get(counter);
                SafeQuestion q = new SafeQuestion();
                String question = mQuestions.get(index);
                q.setQuestion(question);
                q.setQuestionId(index);
                qs[j] = q;
                counter++;
            }
            questions.put(i, qs);
        }
        return questions;

    }
    /**
     * @description new获取安全问题
     * @created 2014-9-2 下午3:14:35
     * @author ZZB
     */
    public static SparseArray<SafeQuestion[]> getNewQuestions(List<SafeBean.QuestionListBean> been){
        SparseArray<SafeQuestion[]> questions = new SparseArray<SafeQuestion[]>();
        if (!ListUtils.isEmpty(been)){
            SafeQuestion[] qs = new SafeQuestion[been.size()];
            for (int i=0; i<been.size(); i++) {
                SafeQuestion q = new SafeQuestion();
                String question = been.get(i).getName();
                q.setQuestion(question);
                q.setQuestionId(been.get(i).getID());
                qs[i] = q;
                questions.put(i, qs);
            }
        }
        return questions;

    }
    /**
     * @description 生成18个不重复的随机数：0-23
     * @created 2014-9-2 下午2:59:21
     * @author ZZB
     */
    private static SparseIntArray genRandomIndex(){
        SparseIntArray indexes = new SparseIntArray();
        Set<Integer> set = new LinkedHashSet<Integer>();
        Random rand = new Random();
        int counter = 0;
        while (set.size() < 18){
            int num = rand.nextInt(24) + 1;
            boolean add = set.add(num);
            if(add){
                indexes.put(counter, num);
                counter++;
            }
        }
        return indexes;
    }
}
