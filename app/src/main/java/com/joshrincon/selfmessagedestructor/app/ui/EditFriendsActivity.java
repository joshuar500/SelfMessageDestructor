package com.joshrincon.selfmessagedestructor.app.ui;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.joshrincon.selfmessagedestructor.app.R;
import com.joshrincon.selfmessagedestructor.app.utilities.ParseConstants;
import com.parse.*;

import java.util.List;


public class EditFriendsActivity extends ListActivity {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(com.joshrincon.selfmessagedestructor.app.R.layout.activity_edit_friends);

        setupActionBar();

        // this makes the checkmarks clickable
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        setProgressBarIndeterminateVisibility(true);

        ParseQuery<ParseUser> query = new ParseUser().getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {

                setProgressBarIndeterminateVisibility(false);

                if(e == null) {
                    // Success
                    mUsers = parseUsers;
                    String[] usernames = new String[mUsers.size()];

                    int i = 0;
                    for(ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriendsActivity.this, android.R.layout.simple_list_item_checked, usernames);
                    setListAdapter(adapter);

                    addFriendCheckmarks();
                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(getListView().isItemChecked(position)) {
            // this adds user locally
            mFriendsRelation.add(mUsers.get(position));
            // this saves to parse server
            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        } else {
            // remove friend
            mFriendsRelation.remove(mUsers.get(position));
            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }
    }

    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null) {
                    // list returned - look for a match
                    for(int i=0; i<mUsers.size(); i++) {
                        ParseUser user = mUsers.get(i);

                        for(ParseUser friend  : parseUsers) {
                            if(friend.getObjectId().equals(user.getObjectId())) {
                                getListView().setItemChecked(i, true);
                            }
                        }
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }
}
