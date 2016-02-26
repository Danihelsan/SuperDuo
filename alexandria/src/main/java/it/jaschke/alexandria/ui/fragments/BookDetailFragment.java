package it.jaschke.alexandria.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.ui.activities.MainActivity;


public class BookDetailFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EAN_KEY = "EAN";
    private final int LOADER_ID = 10;
    private String ean;
    private ShareActionProvider shareActionProvider;
    private MenuItem shareItem;
    private View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_full_book, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            ean = arguments.getString(BookDetailFragment.EAN_KEY);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        ButterKnife.findById(rootView,R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(getActivity(), BookService.class);
                bookIntent.putExtra(BookService.EAN, ean);
                bookIntent.setAction(BookService.DELETE_BOOK);
                getActivity().startService(bookIntent);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_detail, menu);

        shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(ean)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!this.isAdded()){
            return;
        }
        if (!data.moveToFirst()) {
            return;
        }

        String bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        TextView textBookTitle = ButterKnife.findById(rootView,R.id.fullBookTitle);
        if (bookTitle!=null){
            textBookTitle.setText(bookTitle);
            loadShareProvider(bookTitle);
        } else{
            textBookTitle.setVisibility(View.INVISIBLE);
            shareItem.setVisible(false);
        }

        loadDetailField(data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE)), (TextView) ButterKnife.findById(rootView,R.id.fullBookSubTitle));
        loadDetailField(data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.DESC)), (TextView) ButterKnife.findById(rootView,R.id.fullBookDesc));


        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        TextView textAuthors = ButterKnife.findById(rootView,R.id.authors);
        if (authors!=null){
            String[] authorsArr = authors.split(",");
            if (authorsArr!=null && authorsArr.length>0){
                textAuthors.setLines(authorsArr.length);
            }
            textAuthors.setText(authors.replace(",","\n"));
        } else{
            textAuthors.setVisibility(View.INVISIBLE);
        }

        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        ImageView fullBookCover = ButterKnife.findById(rootView,R.id.fullBookCover);
        if(imgUrl!=null && Patterns.WEB_URL.matcher(imgUrl).matches()){
            Glide.with(getActivity()).load(imgUrl).into(fullBookCover);
            rootView.findViewById(R.id.fullBookCover).setVisibility(View.VISIBLE);
        } else{
            fullBookCover.setVisibility(View.INVISIBLE);
        }

        loadDetailField(data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY)), (TextView) ButterKnife.findById(rootView,R.id.categories));

        if(rootView.findViewById(R.id.right_container)!=null){
            rootView.findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
        }

    }

    private void loadShareProvider(String title) {
        if (shareActionProvider==null){
            return;
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + title);
        shareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public void onPause() {
        super.onDestroyView();
        if(MainActivity.IS_TABLET && rootView.findViewById(R.id.right_container)==null){
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}