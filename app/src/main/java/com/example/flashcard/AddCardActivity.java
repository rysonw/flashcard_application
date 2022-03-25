package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_card);
        ImageView cancelButtonImageView = findViewById(R.id.cancel_button_imageview);
        ImageView saveButtonImageView = findViewById(R.id.save_button_imageview);
        EditText questionEditText = findViewById(R.id.question_edittext);
        EditText correctAnswerEditText = findViewById(R.id.answer_edittext);
        EditText wrongAnswer1EditText = findViewById(R.id.wronganswer1_edittext);
        EditText wrongAnswer2EditText = findViewById(R.id.wronganswer2_edittext);
        TextInputLayout questionTIL = findViewById(R.id.question_text_input_layout);
        TextInputLayout correctAnswerTIL = findViewById(R.id.answer_text_input_layout);
        TextInputLayout wrongAnswer1TIL = findViewById(R.id.wronganswer1_text_input_layout);
        TextInputLayout wrongAnswer2TIL = findViewById(R.id.wronganswer2_text_input_layout);

        // set the question and answer passed from main activity
        String question = getIntent().getStringExtra("question");
        String correctAnswer = getIntent().getStringExtra("correctAnswer");
        String wrongAnswer1 = getIntent().getStringExtra("wrongAnswer1");
        String wrongAnswer2 = getIntent().getStringExtra("wrongAnswer2");
//        System.out.println("Add correct:"+correctAnswer+"wrong1"+wrongAnswer1+"wrong2"+wrongAnswer2);
        questionEditText.setText(question);
        correctAnswerEditText.setText(correctAnswer);
        wrongAnswer1EditText.setText(wrongAnswer1);
        wrongAnswer2EditText.setText(wrongAnswer2);

        // tap cancel button to move to the main page
        cancelButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // tap save button to save the set of question and answer
        saveButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputQString = questionEditText.getText().toString();
                String inputAString = correctAnswerEditText.getText().toString();
                String inputWrongAnswer1String = wrongAnswer1EditText.getText().toString();
                String inputWrongAnswer2String = wrongAnswer2EditText.getText().toString();

                // check if it is not empty
                if(inputQString.isEmpty() || inputAString.isEmpty() || inputWrongAnswer1String.isEmpty() || inputWrongAnswer2String.isEmpty()) {
                    // show a snackbar message
                    Snackbar.make(saveButtonImageView,
                            "Please fill all fields!",
                            Snackbar.LENGTH_LONG)
                            .show();
                } else if (Objects.equals(question, inputQString) && Objects.equals(correctAnswer, inputAString)
                        && Objects.equals(wrongAnswer1, inputWrongAnswer1String) && Objects.equals(wrongAnswer2, inputWrongAnswer2String)){
                    // when there is no edit made, show a snackbar message
                    Snackbar.make(saveButtonImageView,
                            "No change to the flashcard? Click CANCEL button to leave this page.",
                            Snackbar.LENGTH_LONG)
                            .show();
                } else{
                    Intent data = new Intent();
                    data.putExtra("question",inputQString);
                    data.putExtra("correctAnswer",inputAString);
                    data.putExtra("wrongAnswer1",inputWrongAnswer1String);
                    data.putExtra("wrongAnswer2",inputWrongAnswer2String);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        // show the error when user leaves the edit field blank
        class emptyError implements TextWatcher {
            private View view;
            private emptyError(View view) {
                this.view = view;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Nothing */ }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){ /* Nothing */ }

            @Override
            public void afterTextChanged(Editable s) {
                switch (view.getId()){
                    case R.id.question_edittext:
                        if (s.length() == 0) {
                            questionTIL.setError("Required");
                            questionTIL.setErrorEnabled(true);
                        } else {
                            questionTIL.setErrorEnabled(false);
                            questionTIL.setError(null);
                        }
                        break;
                    case R.id.answer_edittext:
                        if (s.length() == 0) {
                            correctAnswerTIL.setError("Required");
                            correctAnswerTIL.setErrorEnabled(true);
                        } else {
                            correctAnswerTIL.setErrorEnabled(false);
                            correctAnswerTIL.setError(null);
                        }
                        break;
                    case R.id.wronganswer1_edittext:
                        if (s.length() == 0) {
                            wrongAnswer1TIL.setError("Required");
                            wrongAnswer1TIL.setErrorEnabled(true);
                        } else {
                            wrongAnswer1TIL.setErrorEnabled(false);
                            wrongAnswer1TIL.setError(null);
                        }
                        break;
                    case R.id.wronganswer2_edittext:
                        if (s.length() == 0) {
                            wrongAnswer2TIL.setError("Required");
                            wrongAnswer2TIL.setErrorEnabled(true);
                        } else {
                            wrongAnswer2TIL.setErrorEnabled(false);
                            wrongAnswer2TIL.setError(null);
                        }
                        break;
                    }
                }
            }
        questionEditText.addTextChangedListener(new emptyError(questionEditText));
        correctAnswerEditText.addTextChangedListener(new emptyError(correctAnswerEditText));
        wrongAnswer1EditText.addTextChangedListener(new emptyError(wrongAnswer1EditText));
        wrongAnswer2EditText.addTextChangedListener(new emptyError(wrongAnswer2EditText));
    }
}