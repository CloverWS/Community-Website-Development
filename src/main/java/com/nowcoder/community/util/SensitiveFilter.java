package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换常量
    private static final String REPLACEMENT = "***";

    // 根节点初始化
    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init(){
        // 在target的class目录下寻找资源
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ){
            String keyword;
            while ((keyword = reader.readLine()) != null){
                this.addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 过滤敏感词
     * @param text 带过滤的文本
     * @return 过滤后的文本，敏感词用“***”代替
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }

        // 指针 1
        TrieNode tempNode = root;
        // 指针 2
        int begin = 0;
        // 指针 3
        int position = 0;
        // 结果, 存放过滤后的文本，比String效率高
        StringBuilder sb = new StringBuilder();

        // position会比指针2更先到达text.length，所以需要以position为循环条件
        while(position < text.length()){
            char c = text.charAt(position);

            // 跳过符号，例如：
            // 敏感词：嫖娼 ---- 用户输入： *嫖*娼*
            if (isSymbol(c)){
                // 若指针1指向根节点，将此符号计入结果，让指针2向下走一步。
                if (tempNode == root){
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间，指针3都向下走一步。
                position ++;
                continue;
            }

            // 不是符号的情况，检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null){
                // 以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = root;

            }else if (tempNode.isKeywordEnd){
                // 发现了敏感词，将begin到position的字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = root;

            } else{
                // 继续检查下一个字符
                position++;
            }
        }

        // 将最后一批字符计入到结果
        // position已经到终点而begin还没有到的情况。
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c){
        // c < 0x2E80 || c > 0x9FFF 东亚文字范围之外
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 添加关键词到前缀树中
    private void addKeyword(String keyword){

        // 指针
        TrieNode tempNode = root;

        for(int i=0; i < keyword.length(); i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null){
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            // 指向子节点，进入下一轮循环
            tempNode = subNode;

            // 设置结束标识
            if (i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    // 定义一个前缀树
    private class TrieNode{

        // 关键词结束的标识
        private boolean isKeywordEnd = false;

        // 当前节点的子节点
        // key : 下级节点的字符 value ： 下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c,node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }


    }

}
