package com.example.fourdle

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.internal.ViewUtils.hideKeyboard

class MainActivity : AppCompatActivity()
{
    var chosen: String = ""
    var mediaPlayer: MediaPlayer? = null
    var musicOn: Boolean = true
    var musicNumber = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        var lives = 3
        var counter = 3
        chosen = FourLetterWordList.FourLetterWordList.getRandomFourLetterWord().uppercase()
        Log.d("tag", chosen)
        Log.d("vasanth", chosen)

        val entry1 = findViewById<TextView>(R.id.entry1)
        val entry2 = findViewById<TextView>(R.id.entry2)
        val entry3 = findViewById<TextView>(R.id.entry3)

        val correction1 = findViewById<TextView>(R.id.correction1)
        val correction2 = findViewById<TextView>(R.id.correction2)
        val correction3 = findViewById<TextView>(R.id.correction3)

        val life3 = findViewById<ImageView>(R.id.life3)
        val life2 = findViewById<ImageView>(R.id.life2)
        val life1 = findViewById<ImageView>(R.id.life1)

        val guess = findViewById<EditText>(R.id.guess)






        //mediaPlayer = MediaPlayer().create(requireContext(), R.raw.three)



        val help = findViewById<ImageView>(R.id.help)

        help.setOnClickListener(){



        }

        val music = findViewById<ImageView>(R.id.music)
        music.setImageResource(R.drawable.unmute)
        play()

        music.setOnClickListener(){
            if (musicOn == true)
            {
                pause()

                music.setImageResource(R.drawable.mute)
                musicOn = false
            }
            else
            {
                play()
                music.setImageResource(R.drawable.unmute)
                musicOn = true
            }

        }



        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {



            if (guess.text.length != 4)
            {
                Toast.makeText(it.context, "enter 4 letter word", Toast.LENGTH_SHORT).show()
                hideKeyboard()
                return@setOnClickListener
            }

            if (!isLetters(guess.text.toString()))
            {
                Toast.makeText(it.context, "enter letters (a-z)", Toast.LENGTH_SHORT).show()
                hideKeyboard()
                return@setOnClickListener
            }

            if (guess.text.toString().uppercase() == chosen)
            {

                entry1.text = "CONGRATS"
                entry1.setBackgroundColor(resources.getColor(R.color.play))
                correction1.setText(null)

                entry2.text = "YOU"
                entry2.setBackgroundColor(resources.getColor(R.color.play))
                correction2.setText(null)

                entry3.text = "WIN!"
                entry3.setBackgroundColor(resources.getColor(R.color.play))
                correction3.setText(null)


                guess.text.clear()
                guess.setFocusable(false)
                guess.setHint("target word: $chosen")
                //finish();
                //startActivity(getIntent());
                //recreate()

                stopSound()
                musicNumber = 4
                play()

                button.setText("Play Again")
                button.setBackgroundColor(resources.getColor(R.color.play))
                button.setOnClickListener(){
                    stopSound()
                    recreate()
                }

                hideKeyboard()
                return@setOnClickListener

            }


            val correction: SpannableStringBuilder = checkGuess(guess.text.toString().uppercase())


            if (counter == 3)
            {
                entry1.text = guess.text.toString().uppercase()
                correction1.text = correction
            }
            else if(counter == 2)
            {
                entry2.text = guess.text.toString().uppercase()
                correction2.text = correction
            }
            else
            {
                entry3.text = guess.text.toString().uppercase()
                correction3.text = correction
            }

            guess.text.clear()


            if (guess.text.toString().uppercase() != chosen)
            {
                if (counter == 3)
                {
                    life3.setImageResource(android.R.color.transparent);
                    stopSound()
                    musicNumber = 2
                    play()
                }
                else if (counter == 2)
                {
                    life2.setImageResource(android.R.color.transparent);
                    stopSound()
                    musicNumber = 1
                    play()
                }
                else
                {
                    life1.setImageResource(android.R.color.transparent);
                }

                lives--
                counter--

            }

            hideKeyboard()

            if (lives == 0)
            {
                guess.setFocusable(false)
                guess.setHint("target word: $chosen")
                //finish();
                //startActivity(getIntent());
                //recreate()

                stopSound()
                musicNumber = 5
                play()

                button.setText("Play Again")
                button.setBackgroundColor(resources.getColor(R.color.play))
                button.setOnClickListener(){
                    stopSound()
                    recreate()
                }
            }


        }


    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isLetters(string: String): Boolean {
        return string.all { it.isLetter() }
    }


    fun play()
    {
        if (mediaPlayer == null)
        {
            if(musicNumber == 3)
            {
                mediaPlayer = MediaPlayer.create(this, R.raw.three)
            }
            else if(musicNumber == 2)
            {
                mediaPlayer = MediaPlayer.create(this, R.raw.two)
            }
            else if(musicNumber == 1)
            {
                mediaPlayer = MediaPlayer.create(this, R.raw.one)
            }
            else if(musicNumber == 4)
            {
                mediaPlayer = MediaPlayer.create(this, R.raw.win)
            }
            else if(musicNumber == 5)
            {
                mediaPlayer = MediaPlayer.create(this, R.raw.lose)
            }

            //mediaPlayer = MediaPlayer.create(this, R.raw.one)
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.start()
        }
        else
        {
            mediaPlayer!!.start()
        }
    }

    fun pause()
    {
        if (mediaPlayer?.isPlaying == true)
        {
            mediaPlayer?.pause()
        }
    }

    fun stopSound()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }


    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String) : SpannableStringBuilder
    {
        var result = ""

        val checker = mutableListOf<Int>()
        val tracker = mutableListOf<Char>()

        for (i in 0..3)
        {
            tracker.add(chosen[i])
        }

        for (i in 0..3)
        {
            if (guess[i] == chosen[i])
            {
                result += guess[i]
                checker.add(0)

                tracker.remove(chosen[i])
            }
//            else if (guess[i] in tracker)
//            {
//                result += guess[i]
//                checker.add(1)
//
//            }
            else
            {
                result += guess[i]
                checker.add(2)
            }
        }

        for(i in 0..3)
        {
            if (guess[i] in tracker)
            {
                checker.set(i,1)
                tracker.remove(guess[i])
            }

        }

        //val spannableStringBuilder = SpannableStringBuilder(result)
        val spannableStringBuilder = SpannableStringBuilder("")


        for (i in 0..3)
        {
            val color0 = BackgroundColorSpan(resources.getColor(R.color.play))
            val color1 = BackgroundColorSpan(resources.getColor(R.color.within))
            val color2 = BackgroundColorSpan(resources.getColor(R.color.miss))

            Log.d("tag", "$i: " + checker.get(i).toString())
            if (checker.get(i) == 0)
            {
                spannableStringBuilder.append(result[i])
                spannableStringBuilder.setSpan(color0, i, spannableStringBuilder.length, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            else if (checker.get(i) == 1)
            {
                spannableStringBuilder.append(result[i])
                spannableStringBuilder.setSpan(color1, i, spannableStringBuilder.length, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            else
            {
                spannableStringBuilder.append(result[i])
                spannableStringBuilder.setSpan(color2, i, spannableStringBuilder.length, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }


        return spannableStringBuilder
    }




}