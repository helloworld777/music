package com.lu.library.recyclerview.help;

/**
  *@date on 2017/12/6
  *@author pengfeng
  *@describe
  */
public interface ItemTouchHelperAdapter {


    boolean onItemMove(int fromPosition, int toPosition);


    void onItemDismiss(int position);
}