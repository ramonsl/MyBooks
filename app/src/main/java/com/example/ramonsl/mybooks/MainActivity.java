package com.example.ramonsl.mybooks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BooksTask mTask;
    ArrayList<Books> mMyBooks;
    ListView mListViewBooks;
    TextView mTxtMsg;
    ProgressBar mProgessBar;
    ArrayAdapter<Books> mAdapter;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtMsg = findViewById(R.id.txtEmpty);
        mProgessBar = findViewById(R.id.progressBar);
        mListViewBooks = findViewById(R.id.listBooks);
        mListViewBooks.setEmptyView(mTxtMsg);

        if (BooksHttp.hasConnected(this)) {
            final SearchView search = findViewById(R.id.search);
            BooksHttp.setUrl("android", 10);
            buscarLivros();
        } else {
            Toast.makeText(this, "Sem Conexao", Toast.LENGTH_LONG).show();
        }

    }


    private void showProgress(boolean show) {
        if (show) {
            mTxtMsg.setText("Buscando Dados");
        }
        mTxtMsg.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgessBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void startDownload() {
        mTask = new BooksTask();
        mTask.execute();

    }

    private void buscarLivros() {

        if (mMyBooks == null) {
            mMyBooks = new ArrayList<Books>();
        }
        // mAdapter = new ArrayAdapter<Books>(this,android.R.layout.simple_list_item_1,mMyBooks);

        mAdapter = new BooksListAdapter(getApplicationContext(), mMyBooks);
        mListViewBooks.setAdapter(mAdapter);

        if (mTask == null) {
            if (BooksHttp.hasConnected(this)) {
                startDownload();
            } else {
                mTxtMsg.setText("Sem conexão");
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            showProgress(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //Carrega o arquivo de menu.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
      //Pega o Componente.
        SearchView mSearchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        //Define um texto de ajuda:
        mSearchView.setQueryHint("Digite sua busca");
        // exemplos de utilização:
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BooksHttp.setUrl(query, 10);
                startDownload();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    class BooksTask extends AsyncTask<Void, Void, ArrayList<Books>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected ArrayList<Books> doInBackground(Void... strings) {
            ArrayList<Books> bookList = BooksHttp.loadBooks();
            return bookList;
        }

        @Override
        protected void onPostExecute(ArrayList<Books> books) {
            super.onPostExecute(books);
            showProgress(false);
            if (books != null) {
                mMyBooks.clear();
                mMyBooks.addAll(books);
                if(books.size()==0){
                    mTxtMsg.setText("Não encontramos resultados. Informe uma nova Busca");
                }
                mAdapter.notifyDataSetChanged();
            } else {
                mTxtMsg.setText("Erro ao obter dados");
            }
        }
    }
}
