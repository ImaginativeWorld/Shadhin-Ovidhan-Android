/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class greek_alphabet_activity extends AppCompatActivity implements View.OnClickListener {


    // Views
    ListView mainList;
    Button btnClose;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.greek_alphabet_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //initialize the items
        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(greek_alphabet_activity.this);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }

        });


        //===================================================


        mainList = (ListView) findViewById(R.id.list_main);

        ArrayList<HashMap<String, String>> greekAlpList = new ArrayList<>();
        HashMap<String, String> listItem = new HashMap<>();

        listItem.put("capital_letter", "A");
        listItem.put("small_letter", "α");
        listItem.put("pronounce", "alpha");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Β");
        listItem.put("small_letter", "β");
        listItem.put("pronounce", "beta");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Γ");
        listItem.put("small_letter", "γ");
        listItem.put("pronounce", "gamma");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Δ");
        listItem.put("small_letter", "δ");
        listItem.put("pronounce", "delta");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ε");
        listItem.put("small_letter", "ε");
        listItem.put("pronounce", "epsilon");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ζ");
        listItem.put("small_letter", "ζ");
        listItem.put("pronounce", "zeta");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Η");
        listItem.put("small_letter", "η");
        listItem.put("pronounce", "eta");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Θ");
        listItem.put("small_letter", "θ");
        listItem.put("pronounce", "theta");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ι");
        listItem.put("small_letter", "ι");
        listItem.put("pronounce", "iota");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Κ");
        listItem.put("small_letter", "κ");
        listItem.put("pronounce", "kappa");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Λ");
        listItem.put("small_letter", "λ");
        listItem.put("pronounce", "lambda");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Μ");
        listItem.put("small_letter", "μ");
        listItem.put("pronounce", "mu");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ν");
        listItem.put("small_letter", "ν");
        listItem.put("pronounce", "nu");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ξ");
        listItem.put("small_letter", "ξ");
        listItem.put("pronounce", "xi");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ο");
        listItem.put("small_letter", "ο");
        listItem.put("pronounce", "omicron");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Π");
        listItem.put("small_letter", "π");
        listItem.put("pronounce", "pi");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ρ");
        listItem.put("small_letter", "ρ");
        listItem.put("pronounce", "rho");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Σ");
        listItem.put("small_letter", "σ");
        listItem.put("pronounce", "sigma");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Τ");
        listItem.put("small_letter", "τ");
        listItem.put("pronounce", "tau");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Υ");
        listItem.put("small_letter", "υ");
        listItem.put("pronounce", "upsilon");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Φ");
        listItem.put("small_letter", "φ");
        listItem.put("pronounce", "phi");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Χ");
        listItem.put("small_letter", "χ");
        listItem.put("pronounce", "chi");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ψ");
        listItem.put("small_letter", "ψ");
        listItem.put("pronounce", "psi");

        greekAlpList.add(listItem);

        listItem = new HashMap<>();

        listItem.put("capital_letter", "Ω");
        listItem.put("small_letter", "ω");
        listItem.put("pronounce", "omega");

        greekAlpList.add(listItem);


        SimpleAdapter simpleAdapter = new SimpleAdapter(greek_alphabet_activity.this, greekAlpList,
                R.layout.list_item_greek_alphabet,
                new String[]{"capital_letter", "small_letter", "pronounce"},
                new int[]{R.id.capital_letter, R.id.small_letter, R.id.pronounce});
        mainList.setAdapter(simpleAdapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.pronounce);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(textView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    textToSpeech.speak(textView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }


            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;
        }
    }
}


//        listItem.clear();

        /*
        ID	CLetter	SLetter	Pronounce
1	Α	α	alpha
2	Β	β	beta
3	Γ	γ	gamma
4	Δ	δ	delta
5	Ε	ε	epsilon
6	Ζ	ζ	zeta
7	Η	η	eta
8	Θ	θ	theta
9	Ι	ι	iota
10	Κ	κ	kappa
11	Λ	λ	lambda
12	Μ	μ	mu
13	Ν	ν	nu
14	Ξ	ξ	xi
15	Ο	ο	omicron
16	Π	π	pi
17	Ρ	ρ	rho
18	Σ	σ	sigma
19	Τ	τ	tau
20	Υ	υ	upsilon
21	Φ	φ	phi
22	Χ	χ	chi
23	Ψ	ψ	psi
24	Ω	ω	omega


         */