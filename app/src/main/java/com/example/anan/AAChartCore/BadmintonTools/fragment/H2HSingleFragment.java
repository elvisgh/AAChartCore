package com.example.anan.AAChartCore.BadmintonTools.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anan.AAChartCore.BadmintonTools.adapter.GameRecordAdapter;
import com.example.anan.AAChartCore.BadmintonTools.data.DBUtil;
import com.example.anan.AAChartCore.BadmintonTools.data.Game;
import com.example.anan.AAChartCore.BadmintonTools.tool.FileChooseUtil;
import com.example.anan.AAChartCore.BadmintonTools.tool.ReadExcelUtil;
import com.example.anan.AAChartCore.R;

import java.util.ArrayList;
import java.util.List;

public class H2HSingleFragment extends Fragment {
    ListView listView;
    List<String> data = new ArrayList<>();
    GameRecordAdapter gameRecordAdapter;

    private static final String[] COUNTRIES = new String[]{
            "杰斯","露露","罗乾","云儿"
    };

    TextView score_12, score_34;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_head2head_single, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gameRecordAdapter = new GameRecordAdapter(data, getActivity());
        listView = (ListView)getActivity().findViewById(R.id.single_game_records);
        listView.setAdapter(gameRecordAdapter);

        TextView emptyListView = (TextView) getActivity().findViewById(R.id.empty_view);
        listView.setEmptyView(emptyListView);

        final AutoCompleteTextView textView1 = (AutoCompleteTextView) getActivity().findViewById(R.id.single_game_name_1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,COUNTRIES);
        textView1.setAdapter(adapter1);

        final AutoCompleteTextView textView4 = (AutoCompleteTextView) getActivity().findViewById(R.id.single_game_name_4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,COUNTRIES);
        textView4.setAdapter(adapter4);

        score_12 = (TextView)getActivity().findViewById(R.id.single_game_score_12);
        score_34 = (TextView)getActivity().findViewById(R.id.single_game_score_34);

        final ImageView exitApp = (ImageView)getActivity().findViewById(R.id.tv_back);
        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        final ImageView refreshDb = (ImageView)getActivity().findViewById(R.id.tv_operation);
        refreshDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        Button button = (Button)getActivity().findViewById(R.id.single_game_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Game> games = new ArrayList<>();
                games = DBUtil.GameDBManager.getInstance().getGameRecordByPlayer(textView1.getText().toString());

                int aWinCount = 0;
                int dWinCount = 0;

                data.clear();
                List<String> data1 = new ArrayList<>();

                Log.i("xxx", textView1.getText().toString() + textView4.getText().toString());
                for (Game game : games) {
                    if ((game.getPlayer_1().equals(textView1.getText().toString()) || game.getPlayer_2().equals(textView1.getText().toString()))
                            && (game.getPlayer_3().equals(textView4.getText().toString()) || game.getPlayer_4().equals(textView4.getText().toString()))) {
                        Log.i("xxx", game.toString());
                        data1.add(game.toString());
                        if (game.getScore_12() > game.getScore_34()) {
                            aWinCount++;
                        } else {
                            dWinCount++;
                        }
                    }
                }
                data.addAll(data1);
                Log.i("xxx", "胜率: A " + aWinCount + " : " + dWinCount + " C");
                score_12.setText(aWinCount+"");
                score_34.setText(dWinCount+"");

                handler.sendEmptyMessage(1);
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (1 == message.what) {
                gameRecordAdapter.notifyDataSetChanged();
            }

            return false;
        }
    });

    private static final int FILE_SELECT_CODE = 0;
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Log.d("xxx", "亲，木有文件管理器啊-_-!!");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        String path;

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();

                Log.d("xxx", "返回结果1: " + path);
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = FileChooseUtil.getPath(getActivity(), uri);

                Log.d("xxx", "返回结果2: " + path);

            } else {//4.4以下下系统调用方法
                path = FileChooseUtil.getRealPathFromURI(uri);

                Log.d("xxx", "返回结果3: " + path);

            }

            ReadExcelUtil.getInstance(getContext()).readExcel(path);
        }
    }
}
