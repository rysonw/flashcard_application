package com.example.flashcard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    // global variables able to access variables in all methods of MainActivity

    boolean answerChoicesVisible = true; // true if multiple choices are visible
    boolean emptyState = false; // true if empty state is available


    // a list of integer for choice index
    List<Integer> choiceList = Arrays.asList(1,2,3);

    // initialized the index of correct / wrong answer choices
    int wrongChoice1IndexInt = new Integer(1);
    int correctChoiceIndexInt = new Integer(2);
    int wrongChoice2IndexInt = new Integer(3);

    int addCARD_REQUEST_CODE = 100;
    int editCARD_REQUEST_CODE = 200;
    int firstCARD_REQUEST_CODE = 300;

    int nowDisplayedCardIndexInt = 0; // track the index of displayed card
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards; // holds a list of flashcards
    Flashcard editedCard;



    // create a function to generate a number in the range [minNumber, maxNumber] -- inclusive
    public int getRandomNumber(int minNumber, int maxNumber) {
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }

    // *
    // create a function to set multiple choices
    public int setMultipleChoice (TextView choice1, TextView choice2, TextView choice3,
                                  String correctAnswer, String wrongAnswer1,
                                  String wrongAnswer2){

        Collections.shuffle(choiceList);

        // assign answer choices
        correctChoiceIndexInt = choiceList.get(0);
        wrongChoice1IndexInt = choiceList.get(1);
        wrongChoice2IndexInt = choiceList.get(2);

        if (correctChoiceIndexInt == 1){
            choice1.setText("1. " + correctAnswer);
            if (wrongChoice1IndexInt == 2){
                choice2.setText("2. " + wrongAnswer1);
                choice3.setText("3. " + wrongAnswer2);
            } else {
                choice2.setText("2. " + wrongAnswer2);
                choice3.setText("3. " + wrongAnswer1);
            }
        } else if (correctChoiceIndexInt == 2){
            choice2.setText("2. " + correctAnswer);
            if (wrongChoice1IndexInt == 1){
                choice1.setText("1. " + wrongAnswer1);
                choice3.setText("3. " + wrongAnswer2);
            } else {
                choice1.setText("1. " + wrongAnswer2);
                choice3.setText("3. " + wrongAnswer1);
            }
        } else {
            choice3.setText("3. " + correctAnswer);
            if (wrongChoice1IndexInt == 1) {
                choice1.setText("1. " + wrongAnswer1);
                choice2.setText("2. " + wrongAnswer2);
            } else {
                choice1.setText("1. " + wrongAnswer2);
                choice2.setText("2. " + wrongAnswer1);
            }
        }
        return 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        System.out.println("before: correct"+correctChoiceIndexInt+"wrong1"+wrongChoice1IndexInt+"wrong2"+wrongChoice2IndexInt);

        TextView flashcardQTextView = findViewById(R.id.flashcard_question_textview);
        TextView flashcardATextView = findViewById(R.id.flashcard_answer_textview);
        TextView answerChoice1TextView = findViewById(R.id.answerChoice1_textview);
        TextView answerChoice2TextView = findViewById(R.id.answerChoice2_textview);
        TextView answerChoice3TextView = findViewById(R.id.answerChoice3_textview);
        TextView createFirstCardTextView = findViewById(R.id.create_flashcard_textview);
        TextView emptyStateTextView = findViewById(R.id.empty_state_textview);

        ImageView toggleImageView = findViewById(R.id.toggle_choices_visibility_imageview);
        ImageView addQuestionImageView = findViewById(R.id.add_button_imageview);
        ImageView editQuestionImageView = findViewById(R.id.edit_button_imageview);
        ImageView nextQuestionImageView = findViewById(R.id.next_button_imageview);
        ImageView prevQuestionImageView = findViewById(R.id.prev_button_imageview);
        ImageView deleteQuestionImageView = findViewById(R.id.delete_button_imageview);
        ImageView emptyStateImageView = findViewById(R.id.empty_state_imageview);

        // after application is initialized, fetch updated flashcards
        flashcardDatabase = new FlashcardDatabase(getApplicationContext()); // or this
        allFlashcards = flashcardDatabase.getAllCards();

        // if there is no card
        if (allFlashcards.size() == 0) {
            emptyStateImageView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.VISIBLE);
            createFirstCardTextView.setVisibility(View.VISIBLE);

            flashcardQTextView.setVisibility(View.INVISIBLE);
            flashcardATextView.setVisibility(View.INVISIBLE);
            answerChoice1TextView.setVisibility(View.INVISIBLE);
            answerChoice2TextView.setVisibility(View.INVISIBLE);
            answerChoice3TextView.setVisibility(View.INVISIBLE);
            toggleImageView.setVisibility(View.INVISIBLE);
            addQuestionImageView.setVisibility(View.INVISIBLE);
            editQuestionImageView.setVisibility(View.INVISIBLE);
            nextQuestionImageView.setVisibility(View.INVISIBLE);
            prevQuestionImageView.setVisibility(View.INVISIBLE);
            deleteQuestionImageView.setVisibility(View.INVISIBLE);
            answerChoicesVisible = false;
            emptyState = true;

        //or only 1 flashcard, hide next / previous question
        } else if (allFlashcards.size() == 1) {
                nextQuestionImageView.setVisibility(View.INVISIBLE);
                prevQuestionImageView.setVisibility(View.INVISIBLE);
                emptyState = false;
            }

        // if flashcards exist, display the saved flashcard
        if (allFlashcards != null && allFlashcards.size() > 0) {

            //update the index of the first card to be displayed
            nowDisplayedCardIndexInt = getRandomNumber(0, allFlashcards.size() - 1);

            // get currently displayed flashcard's info
            Flashcard firstCard = allFlashcards.get(nowDisplayedCardIndexInt);

            // set the question and answer TextViews with data from the database
            flashcardQTextView.setText("Q. "+firstCard.getQuestion());
            flashcardATextView.setText("A. "+firstCard.getAnswer());

            // set multiple choices
            setMultipleChoice (answerChoice1TextView, answerChoice2TextView,
                    answerChoice3TextView, firstCard.getAnswer(),
                    firstCard.getWrongAnswer1(),firstCard.getWrongAnswer2());
        }

        // tap on question card to show the answer of flashcard
        flashcardQTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQTextView.setVisibility(View.INVISIBLE);
                flashcardATextView.setVisibility(View.VISIBLE);
            }
        });

        // tap on answer card to go back to the question of flashcard
        flashcardATextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardATextView.setVisibility(View.INVISIBLE);
                flashcardQTextView.setVisibility(View.VISIBLE);
            }
        });


        // light up the answer in green when user tap the correct answer
        // but in red otherwise -- the correct answer is shown in green background
        // the other wrong answer is remained to be white, so that when user tap multiple times,
        // the light is always set
        answerChoice1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correctChoiceIndexInt == 1){
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                } else if (correctChoiceIndexInt == 2){
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.sheer_red, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                } else {
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.sheer_red, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                }
            }
        });

        answerChoice2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correctChoiceIndexInt == 1){
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.sheer_red, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                } else if (correctChoiceIndexInt == 2){
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                } else {
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.sheer_red, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                }
            }
        });

        answerChoice3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correctChoiceIndexInt == 1){
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.sheer_red, null));
                } else if (correctChoiceIndexInt == 2){
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.sheer_red, null));
                } else {
                    answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                    answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                    answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.green, null));
                }
            }
        });

        // tap on toggle button to show or hide answer choices
        toggleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerChoicesVisible) {
                    answerChoice1TextView.setVisibility(View.INVISIBLE);
                    answerChoice2TextView.setVisibility(View.INVISIBLE);
                    answerChoice3TextView.setVisibility(View.INVISIBLE);
                    toggleImageView.setImageResource(R.drawable.show_view);
                    answerChoicesVisible = false;
                } else {
                    answerChoice1TextView.setVisibility(View.VISIBLE);
                    answerChoice2TextView.setVisibility(View.VISIBLE);
                    answerChoice3TextView.setVisibility(View.VISIBLE);
                    toggleImageView.setImageResource(R.drawable.hide_view);
                    answerChoicesVisible = true;
                }
            }
        });

        //tap on the background to reset all views to default settings
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emptyState){

                //set multiple choices' visibility
                answerChoicesVisible = true;
                answerChoice1TextView.setVisibility(View.VISIBLE);
                answerChoice2TextView.setVisibility(View.VISIBLE);
                answerChoice3TextView.setVisibility(View.VISIBLE);
                toggleImageView.setImageResource(R.drawable.hide_view);

                // set Question and answer cards' visibility
                flashcardATextView.setVisibility(View.INVISIBLE);
                flashcardQTextView.setVisibility(View.VISIBLE);

                //set multiple choices' color
                answerChoice1TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                answerChoice2TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                answerChoice3TextView.setBackgroundColor(getResources().getColor(R.color.white, null));
                }
            }
        });

        // tap plus button to move to a card create page
        addQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, addCARD_REQUEST_CODE);
            }
        });

        // from empty state to move to a card create page
        createFirstCardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, firstCARD_REQUEST_CODE);
            }
        });

        // tap edit button to move to edit page
        editQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // find the currently shown card on the deck now
                editedCard = allFlashcards.get(nowDisplayedCardIndexInt);

                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra("question",flashcardQTextView.getText().toString().substring(3));
                intent.putExtra("correctAnswer",flashcardATextView.getText().toString().substring(3));

                // based on answer choice's index, receive different input
                if (correctChoiceIndexInt == 1) {
                    intent.putExtra("wrongAnswer1", answerChoice2TextView.getText().toString().substring(3));
                    intent.putExtra("wrongAnswer2", answerChoice3TextView.getText().toString().substring(3));
                } else if (correctChoiceIndexInt == 2){
                    intent.putExtra("wrongAnswer1",answerChoice1TextView.getText().toString().substring(3));
                    intent.putExtra("wrongAnswer2",answerChoice3TextView.getText().toString().substring(3));
                } else {
                    intent.putExtra("wrongAnswer1",answerChoice1TextView.getText().toString().substring(3));
                    intent.putExtra("wrongAnswer2",answerChoice2TextView.getText().toString().substring(3));
                }
                startActivityForResult(intent, editCARD_REQUEST_CODE);
            }
        });

        // tap next button to move to a different flashcard
        nextQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // don't try to go to next card if you have no cards to begin with or there's single card
                if (allFlashcards.size() == 1){
                    return;
                }

                // initialize currently displayed card's index
                int cardToDisplayIndex = getRandomNumber(0, allFlashcards.size() - 1);

                // find another card if the next card is the same as the current displayed one
                while (cardToDisplayIndex == nowDisplayedCardIndexInt) {
                    cardToDisplayIndex = getRandomNumber(0, allFlashcards.size() - 1);
                }

                nowDisplayedCardIndexInt = cardToDisplayIndex;

//                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
//                if(nowDisplayedCardIndexInt >= allFlashcards.size()) {
//                    Snackbar.make(v,
//                            "It is the last flash card. ",
//                            Snackbar.LENGTH_SHORT)
//                            .show();
//                    nowDisplayedCardIndexInt = 0; // reset index to go back to the beginning
//                };

                // get currently displayed flashcard's info
                Flashcard currentCard = allFlashcards.get(nowDisplayedCardIndexInt);

                // set the question and answer TextViews with data from the database
                flashcardQTextView.setText("Q. "+currentCard.getQuestion());
                flashcardATextView.setText("A. "+currentCard.getAnswer());

                // set multiple choices
                setMultipleChoice (answerChoice1TextView, answerChoice2TextView,
                        answerChoice3TextView, currentCard.getAnswer(),
                        currentCard.getWrongAnswer1(),currentCard.getWrongAnswer2());

            }});


        // tap previous button to move to a different flashcard
        prevQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // don't try to go to previous card if you have no cards to begin with or there's single card
                if (allFlashcards.size() == 1){
                    return;
                }

                // initialize currently displayed card's inex
                int cardToDisplayIndex2 = getRandomNumber(0, allFlashcards.size() - 1);

                // find another card if the next card is the same as the current displayed one
                while (cardToDisplayIndex2 == nowDisplayedCardIndexInt) {
                    cardToDisplayIndex2 = getRandomNumber(0, allFlashcards.size() - 1);
                }

                nowDisplayedCardIndexInt = cardToDisplayIndex2;

                // get currently displayed flashcard's info
                Flashcard currentCard = allFlashcards.get(nowDisplayedCardIndexInt);

                // set the question and answer TextViews with data from the database
                flashcardQTextView.setText("Q. "+currentCard.getQuestion());
                flashcardATextView.setText("A. "+currentCard.getAnswer());

                // set multiple choices
                setMultipleChoice (answerChoice1TextView, answerChoice2TextView,
                        answerChoice3TextView, currentCard.getAnswer(),
                        currentCard.getWrongAnswer1(),currentCard.getWrongAnswer2());

            }});

        // delete question
        deleteQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove the first 3 letters (i.e., "Q. ") to detect the card in database
                flashcardDatabase.deleteCard(flashcardQTextView.getText().toString().substring(3));
                allFlashcards = flashcardDatabase.getAllCards();

                //if there is no card after deletion, show empty state
                if (allFlashcards.isEmpty()){
                    // make everything invisible
                    flashcardATextView.setVisibility(View.INVISIBLE);
                    flashcardQTextView.setVisibility(View.INVISIBLE);
                    answerChoice1TextView.setVisibility(View.INVISIBLE);
                    answerChoice2TextView.setVisibility(View.INVISIBLE);
                    answerChoice3TextView.setVisibility(View.INVISIBLE);
                    addQuestionImageView.setVisibility(View.INVISIBLE);
                    editQuestionImageView.setVisibility(View.INVISIBLE);
                    toggleImageView.setVisibility(View.INVISIBLE);
                    nextQuestionImageView.setVisibility(View.INVISIBLE);
                    prevQuestionImageView.setVisibility(View.INVISIBLE);
                    deleteQuestionImageView.setVisibility(View.INVISIBLE);
                    answerChoicesVisible = false;

                    // show empty state
                    emptyState = true;
                    emptyStateImageView.setVisibility(View.VISIBLE);
                    emptyStateTextView.setVisibility(View.VISIBLE);
                    createFirstCardTextView.setVisibility(View.VISIBLE);
                } else {

                    // randomly display a new card in the deck
                    nowDisplayedCardIndexInt = getRandomNumber(0, allFlashcards.size() - 1);

//                    // display previous card in the deck
//                    nowDisplayedCardIndexInt --;
//
//                    // if the deleted card was the first card, show the last card instead
//                    if (nowDisplayedCardIndexInt == -1) {
//                        nowDisplayedCardIndexInt = allFlashcards.size() - 1;
//                    }

                    // display the card
                    Flashcard flashcard = allFlashcards.get(nowDisplayedCardIndexInt);
                    flashcardQTextView.setText("Q. "+flashcard.getQuestion());
                    flashcardATextView.setText("A. "+flashcard.getAnswer());
                    // set multiple choices
                    // based on the shuffled result, set choices in corresponding textview
                    setMultipleChoice (answerChoice1TextView, answerChoice2TextView,
                            answerChoice3TextView, flashcard.getAnswer(),
                            flashcard.getWrongAnswer1(),flashcard.getWrongAnswer2());

                    // when there is only one remaining flashcard, make the buttons invisible
                    if (allFlashcards.size() == 1) {
                        nextQuestionImageView.setVisibility(View.INVISIBLE);
                        prevQuestionImageView.setVisibility(View.INVISIBLE);
                    }
            }}
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        TextView flashcardQTextView = findViewById(R.id.flashcard_question_textview);
        TextView flashcardATextView = findViewById(R.id.flashcard_answer_textview);
        TextView answerChoice1TextView = findViewById(R.id.answerChoice1_textview);
        TextView answerChoice2TextView = findViewById(R.id.answerChoice2_textview);
        TextView answerChoice3TextView = findViewById(R.id.answerChoice3_textview);

        ImageView toggleImageView = findViewById(R.id.toggle_choices_visibility_imageview);
        ImageView addQuestionImageView = findViewById(R.id.add_button_imageview);
        ImageView editQuestionImageView = findViewById(R.id.edit_button_imageview);
        ImageView deleteQuestionImageView = findViewById(R.id.delete_button_imageview);
        ImageView nextQuestionImageView = findViewById(R.id.next_button_imageview);
        ImageView prevQuestionImageView = findViewById(R.id.prev_button_imageview);

        TextView createFirstCardTextView = findViewById(R.id.create_flashcard_textview);
        TextView emptyStateTextView = findViewById(R.id.empty_state_textview);
        ImageView emptyStateImageView = findViewById(R.id.empty_state_imageview);

//        System.out.println("post: correct"+correctChoiceIndexInt+"wrong1"+wrongChoice1IndexInt+"wrong2"+wrongChoice2IndexInt);


        if (data != null) {
            // key has to match with the one from addcardactivity
            String questionString = data.getExtras().getString("question");
            String correctAnswerString = data.getExtras().getString("correctAnswer");
            String wrongAnswer1String = data.getExtras().getString("wrongAnswer1");
            String wrongAnswer2String = data.getExtras().getString("wrongAnswer2");

            // set the question and answer passed from main activity
            flashcardQTextView.setText("Q. " + questionString);
            flashcardATextView.setText("A. " + correctAnswerString);

            // when resultCode == RESULT_OK
            if (requestCode == addCARD_REQUEST_CODE) {
                // set multiple choices
                // based on the shuffled result, set choices in corresponding textview
                setMultipleChoice (answerChoice1TextView, answerChoice2TextView,
                        answerChoice3TextView, correctAnswerString,
                        wrongAnswer1String,wrongAnswer2String);

                // show a snackbar message
                Snackbar.make(flashcardQTextView,
                        "Card created!",
                        Snackbar.LENGTH_SHORT)
                        .show();

                // insert card to the database
                flashcardDatabase.insertCard(new Flashcard(questionString, correctAnswerString, wrongAnswer1String, wrongAnswer2String));

                // update database
                allFlashcards = flashcardDatabase.getAllCards(); // fetch data from updated flashcards

                // update the card index to the currently added one (last one)
                nowDisplayedCardIndexInt = allFlashcards.size() - 1;

                if (allFlashcards.size() > 1) {
                    nextQuestionImageView.setVisibility(View.VISIBLE);
                    prevQuestionImageView.setVisibility(View.VISIBLE);
                }

            } else if (requestCode == editCARD_REQUEST_CODE){
                // update card
                editedCard.setQuestion(questionString);
                editedCard.setAnswer(correctAnswerString);
                editedCard.setWrongAnswer1(wrongAnswer1String);
                editedCard.setWrongAnswer2(wrongAnswer2String);

                // randomized the multiple choices
                setMultipleChoice (answerChoice1TextView, answerChoice2TextView,
                        answerChoice3TextView, correctAnswerString,
                        wrongAnswer1String,wrongAnswer2String);

                // update database
                flashcardDatabase.updateCard(editedCard);
                allFlashcards = flashcardDatabase.getAllCards();

                // show a snackbar message
                Snackbar.make(flashcardQTextView,
                        "Card successfully edited!",
                        Snackbar.LENGTH_SHORT)
                        .show();

            } else if (requestCode == firstCARD_REQUEST_CODE){
                emptyStateImageView.setVisibility(View.INVISIBLE);
                emptyStateTextView.setVisibility(View.INVISIBLE);
                createFirstCardTextView.setVisibility(View.INVISIBLE);
                emptyState = false;

                flashcardQTextView.setVisibility(View.VISIBLE);
                flashcardATextView.setVisibility(View.VISIBLE);
                answerChoice1TextView.setVisibility(View.VISIBLE);
                answerChoice2TextView.setVisibility(View.VISIBLE);
                answerChoice3TextView.setVisibility(View.VISIBLE);
                toggleImageView.setVisibility(View.VISIBLE);
                addQuestionImageView.setVisibility(View.VISIBLE);
                editQuestionImageView.setVisibility(View.VISIBLE);
                deleteQuestionImageView.setVisibility(View.VISIBLE);

                // show a snackbar message
                Snackbar.make(flashcardQTextView,
                        "Foolay"+("\ud83d\ude4c")+ "Your 1st card is successfully created!",
                        Snackbar.LENGTH_SHORT)
                        .show();

                flashcardDatabase.insertCard(new Flashcard(questionString, correctAnswerString, wrongAnswer1String, wrongAnswer2String));
                allFlashcards = flashcardDatabase.getAllCards(); // fetch data from updated flashcards

                // update the card index to the currently added one (last one)
                nowDisplayedCardIndexInt = allFlashcards.size() - 1;

                // set multiple choices
                // based on the shuffled result, set choices in corresponding textview
                setMultipleChoice (answerChoice1TextView, answerChoice2TextView,
                        answerChoice3TextView, correctAnswerString,
                        wrongAnswer1String,wrongAnswer2String);
            }
        }
    }
}