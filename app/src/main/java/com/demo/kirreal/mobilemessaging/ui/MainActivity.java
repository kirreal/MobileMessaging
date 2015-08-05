package com.demo.kirreal.mobilemessaging.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.demo.kirreal.mobilemessaging.R;


public class MainActivity extends Activity {
    private LinearLayout mHeaderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_layout);
        MessageFragment fragment = null;

        if (savedInstanceState != null) {
            fragment = (MessageFragment) getFragmentManager().findFragmentByTag(MessageFragment.TAG);
        } else {
            fragment = new MessageFragment();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.main_container, fragment, MessageFragment.TAG);
        ft.commit();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = super.onCreateView(name, context, attrs);
        mHeaderProgress = (LinearLayout) findViewById(R.id.headerProgress);

        return view;
    }

    public void startProgress() {
        mHeaderProgress.setVisibility(View.VISIBLE);
    }

    public void stopProgress() {
        mHeaderProgress.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
