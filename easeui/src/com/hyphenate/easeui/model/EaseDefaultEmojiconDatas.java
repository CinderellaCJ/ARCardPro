package com.hyphenate.easeui.model;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojicon.Type;
import com.hyphenate.easeui.utils.EaseSmileUtils;

public class EaseDefaultEmojiconDatas {
    
    private static String[] emojis = new String[]{
        EaseSmileUtils.ee_1,
        EaseSmileUtils.ee_2,
        EaseSmileUtils.ee_3,
        EaseSmileUtils.ee_4,
        EaseSmileUtils.ee_5,
        EaseSmileUtils.ee_6,
        EaseSmileUtils.ee_7,
        EaseSmileUtils.ee_8,
        EaseSmileUtils.ee_9,
        EaseSmileUtils.ee_10,
        EaseSmileUtils.ee_11,
        EaseSmileUtils.ee_12,
        EaseSmileUtils.ee_13,
        EaseSmileUtils.ee_14,
        EaseSmileUtils.ee_15,
        EaseSmileUtils.ee_16,
        EaseSmileUtils.ee_17,
        EaseSmileUtils.ee_18,
        EaseSmileUtils.ee_19,
        EaseSmileUtils.ee_20,
        EaseSmileUtils.ee_21,
        EaseSmileUtils.ee_22,
        EaseSmileUtils.ee_23,
        EaseSmileUtils.ee_24,
        EaseSmileUtils.ee_25,
        EaseSmileUtils.ee_26,
        EaseSmileUtils.ee_27,
        EaseSmileUtils.ee_28,
        EaseSmileUtils.ee_29,
        EaseSmileUtils.ee_30,
        EaseSmileUtils.ee_31,
        EaseSmileUtils.ee_32,
        EaseSmileUtils.ee_33,
        EaseSmileUtils.ee_34,
        EaseSmileUtils.ee_35,
       
    };
    
    private static int[] icons = new int[]{
        R.drawable.emoji_00,
        R.drawable.emoji_01,
        R.drawable.emoji_02,
        R.drawable.emoji_03,
        R.drawable.emoji_04,
        R.drawable.emoji_05,
        R.drawable.emoji_06,
        R.drawable.emoji_07,
        R.drawable.emoji_08,
        R.drawable.emoji_09,
        R.drawable.emoji_11,
        R.drawable.emoji_12,
        R.drawable.emoji_13,
        R.drawable.emoji_14,
        R.drawable.emoji_15,
        R.drawable.emoji_16,
        R.drawable.emoji_17,
        R.drawable.emoji_18,
        R.drawable.emoji_19,
        R.drawable.emoji_20,
        R.drawable.emoji_21,
        R.drawable.emoji_22,
        R.drawable.emoji_23,
        R.drawable.emoji_24,
        R.drawable.emoji_25,
        R.drawable.emoji_26,
        R.drawable.emoji_27,
        R.drawable.emoji_28,
        R.drawable.emoji_29,
        R.drawable.emoji_30,
        R.drawable.emoji_31,
        R.drawable.emoji_32,
        R.drawable.emoji_33,
        R.drawable.emoji_34,
        R.drawable.emoji_34,
    };
    
    
    private static final EaseEmojicon[] DATA = createData();
    
    private static EaseEmojicon[] createData(){
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], emojis[i], Type.NORMAL);
        }
        return datas;
    }
    
    public static EaseEmojicon[] getData(){
        return DATA;
    }
}
