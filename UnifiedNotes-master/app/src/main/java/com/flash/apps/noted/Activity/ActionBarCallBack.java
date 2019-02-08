package com.flash.apps.noted.Activity;

import android.view.Menu;
import android.view.MenuItem;

import com.flash.apps.noted.R;

class ActionBarCallBack implements android.view.ActionMode.Callback {


    @Override
    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_bold)
        {
            
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode mode) {

    }
}
