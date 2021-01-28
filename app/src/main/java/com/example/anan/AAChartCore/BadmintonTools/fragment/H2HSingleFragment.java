package com.example.anan.AAChartCore.BadmintonTools.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.example.anan.AAChartCore.BadmintonTools.tool.SharedPreferenceUtil;
import com.example.anan.AAChartCore.R;

import java.util.ArrayList;
import java.util.List;

public class H2HSingleFragment extends Fragment {
    ListView listView;
    List<String> data = new ArrayList<>();
    GameRecordAdapter gameRecordAdapter;

    List<String> players = new ArrayList<>();

    TextView score_12, score_34;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_head2head_single, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        players = SharedPreferenceUtil.getList("players");

        gameRecordAdapter = new GameRecordAdapter(data, getActivity());
        listView = (ListView)getActivity().findViewById(R.id.single_game_records);
        listView.setAdapter(gameRecordAdapter);

        TextView emptyListView = (TextView) getActivity().findViewById(R.id.empty_view_single);
        listView.setEmptyView(emptyListView);

        final AutoCompleteTextView playerName1 = (AutoCompleteTextView) getActivity().findViewById(R.id.single_game_name_1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, players);
        playerName1.setAdapter(adapter1);

        final AutoCompleteTextView playerName4 = (AutoCompleteTextView) getActivity().findViewById(R.id.single_game_name_4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, players);
        playerName4.setAdapter(adapter4);

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
                games = DBUtil.GameDBManager.getInstance().getGameRecordByPlayer(playerName1.getText().toString());

                int aWinCount = 0;
                int dWinCount = 0;

                data.clear();
                List<String> tempRecords = new ArrayList<>();

                Log.i("xxx", playerName1.getText().toString() + playerName4.getText().toString());
                for (Game game : games) {
                    if ((game.getPlayer_1().equals(playerName1.getText().toString()) || game.getPlayer_2().equals(playerName1.getText().toString()))
                            && (game.getPlayer_3().equals(playerName4.getText().toString()) || game.getPlayer_4().equals(playerName4.getText().toString()))) {
                        Log.i("xxx", game.toString());
                        tempRecords.add(game.toString());
                        if (game.getScore_12() > game.getScore_34()) {
                            aWinCount++;
                        } else {
                            dWinCount++;
                        }
                    }
                }
                data.addAll(tempRecords);

                score_12.setText(aWinCount+"");
                score_34.setText(dWinCount+"");

                gameRecordAdapter.notifyDataSetChanged();
            }
        });
    }

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
