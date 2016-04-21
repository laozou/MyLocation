package test11.mylocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ListAdapter.FunctionInfo;
import ListAdapter.ListAdapter;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews() {
        ListView mListView= (ListView) findViewById(R.id.list_view);
        final List<FunctionInfo> functionInfo = new ArrayList<>();

        functionInfo.add(new FunctionInfo(R.string.location,R.string.location_describe,LocationActivity.class));
        functionInfo.add(new FunctionInfo(R.string.search,R.string.search_descibe,SearchActivity.class));

        mListView.setAdapter(new ListAdapter(MainActivity.this, functionInfo));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this,functionInfo.get(position).getActivity()));
            }
        });
    }
}
