package com.pledgeapp.pledge.fragments.nonprofitlists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.activities.NonProfitDetailActivity;
import com.pledgeapp.pledge.adapters.NonProfitArrayAdapter;
import com.pledgeapp.pledge.helpers.PledgeModel;
import com.pledgeapp.pledge.models.NonProfit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikhil on 10/14/15.
 */
public abstract class NonProfitListFragment extends Fragment {

    protected PledgeModel mPledgeModel;
    private NonProfitArrayAdapter aNonProfits;
    private PledgeModel.OnResultDelegate<List<NonProfit>> mDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<NonProfit> nonProfits = new ArrayList<>();
        aNonProfits = new NonProfitArrayAdapter(getActivity(), nonProfits);

        mPledgeModel = PledgeApplication.getPledgeModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generic_list, container, false);

        ListView lvNonProfits = (ListView) v.findViewById(R.id.lvList);
        lvNonProfits.setAdapter(aNonProfits);
        lvNonProfits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(NonProfitDetailActivity.getLaunchIntent(getContext(),
                                                                      aNonProfits.getItem(position)));
            }
        });
        lvNonProfits.setEmptyView(v.findViewById(R.id.tvEmpty));

        // TODO(nikhilb): Add EndlessScrollListener when the server supports paging
        mDelegate = new PledgeModel.OnResultDelegate<List<NonProfit>>(getContext(), getUserVisibleHint()) {
            @Override
            public void onQueryComplete(List<NonProfit> result) {
                super.onQueryComplete(result);
                aNonProfits.clear();
                aNonProfits.addAll(result);
            }
        };

        fetchNonProfits(mDelegate);

        return v;
    }

    protected abstract void fetchNonProfits(PledgeModel.OnResultDelegate<List<NonProfit>> delegate);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDelegate.cancel();
    }
}
