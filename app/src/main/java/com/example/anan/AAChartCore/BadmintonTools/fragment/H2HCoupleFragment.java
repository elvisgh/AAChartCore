package com.example.anan.AAChartCore.BadmintonTools.fragment;

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
import android.widget.ListView;
import android.widget.TextView;

import com.example.anan.AAChartCore.BadmintonTools.adapter.GameRecordAdapter;
import com.example.anan.AAChartCore.BadmintonTools.data.DBUtil;
import com.example.anan.AAChartCore.BadmintonTools.data.Game;
import com.example.anan.AAChartCore.BadmintonTools.tool.SharedPreferenceUtil;
import com.example.anan.AAChartCore.R;

import java.util.ArrayList;
import java.util.List;

public class H2HCoupleFragment extends Fragment {
    ListView listView;
    List<String> gameRecords = new ArrayList<>();
    GameRecordAdapter gameRecordAdapter;

    List<String> players = new ArrayList<>();

    TextView score_12, score_34;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_head2head_couple, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        players = SharedPreferenceUtil.getList("players");

        gameRecordAdapter = new GameRecordAdapter(gameRecords, getActivity());
        listView = (ListView)getActivity().findViewById(R.id.couple_game_records);
        listView.setAdapter(gameRecordAdapter);

        TextView emptyListView = (TextView) getActivity().findViewById(R.id.empty_view_couple);
        listView.setEmptyView(emptyListView);

        final AutoCompleteTextView playerName1 = (AutoCompleteTextView) getActivity().findViewById(R.id.couple_game_name_1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, players);
        playerName1.setAdapter(adapter1);

        final AutoCompleteTextView playerName2 = (AutoCompleteTextView) getActivity().findViewById(R.id.couple_game_name_2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, players);
        playerName2.setAdapter(adapter2);

        final AutoCompleteTextView playerName3 = (AutoCompleteTextView) getActivity().findViewById(R.id.couple_game_name_3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, players);
        playerName3.setAdapter(adapter3);

        final AutoCompleteTextView playerName4 = (AutoCompleteTextView) getActivity().findViewById(R.id.couple_game_name_4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, players);
        playerName4.setAdapter(adapter4);

        score_12 = (TextView)getActivity().findViewById(R.id.couple_game_score_12);
        score_34 = (TextView)getActivity().findViewById(R.id.couple_game_score_34);

        Button button = (Button)getActivity().findViewById(R.id.couple_game_run);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Game> games = new ArrayList<>();
                games = DBUtil.GameDBManager.getInstance().getGameRecordByPlayer(playerName1.getText().toString());

                int aWinCount = 0;
                int dWinCount = 0;

                gameRecords.clear();
                List<String> tempRecords = new ArrayList<>();
                Log.i("xxx", playerName1.getText().toString()
                        + playerName2.getText().toString()
                        + playerName3.getText().toString()
                        + playerName4.getText().toString());
                for (Game game : games) {

                    if ((game.getPlayer_1().equals(playerName1.getText().toString()) || game.getPlayer_2().equals(playerName1.getText().toString()))
                            && (game.getPlayer_1().equals(playerName2.getText().toString()) || game.getPlayer_2().equals(playerName2.getText().toString()))
                            && (game.getPlayer_3().equals(playerName3.getText().toString()) || game.getPlayer_4().equals(playerName3.getText().toString()))
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

                gameRecords.addAll(tempRecords);

                score_12.setText(aWinCount+"");
                score_34.setText(dWinCount+"");

                gameRecordAdapter.notifyDataSetChanged();
            }
        });
    }
}