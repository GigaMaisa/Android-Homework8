package com.example.homework8

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.homework8.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var firstNameInput: AppCompatEditText
    private lateinit var lastNameInput: AppCompatEditText
    private lateinit var emailInput: AppCompatEditText
    private lateinit var buttonSaveUser: AppCompatButton
    private lateinit var buttonSaveUserFromUpdate: AppCompatButton
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            firstNameInput = etFirstName
            lastNameInput = etLastName
            emailInput = etEmail
            buttonSaveUser = btnSave
            buttonSaveUserFromUpdate = btnSaveFromUpdate
        }
        saveUser()
    }

    private fun saveUser() {
        if (intent.getStringExtra("SOURCE_ACTIVITY_NAME") == UsersActivity::class.java.toString()) {
            saveUserClick()
        } else {
            getUserFromUpdate()
        }
    }

    private fun getUserFromUpdate() {
        buttonSaveUser.visibility = View.GONE
        buttonSaveUserFromUpdate.visibility = View.VISIBLE

        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable("userEditing", User::class.java)
        } else {
            intent.extras?.getParcelable("userEditing")
        }

         user?.apply {
            firstNameInput.setText(firstName)
            lastNameInput.setText(lastName)
            emailInput.setText(email)
        }

        saveUserFromEditIconClick()
    }

    private fun saveUserFromEditIconClick() {
        buttonSaveUserFromUpdate.setOnClickListener {
            validateFields(firstNameInput, lastNameInput, emailInput)
            val firstName = getString(firstNameInput)
            val lastName = getString(lastNameInput)
            val email = getValidEmail()

            firstName?.let {
                lastName?.let {
                    email?.let {
                        val updatedUser = User(firstName, lastName, email)
                        updatedUser.let {
                            UsersList.listOfUsers[UsersList.listOfUsers.indexOf(user)] = updatedUser
                            goBackToMainActivity()
                        }
                    }
                }
            }
        }
    }

    private fun saveUserClick() {
        buttonSaveUser.visibility = View.VISIBLE
        buttonSaveUserFromUpdate.visibility = View.GONE

        buttonSaveUser.setOnClickListener {
            validateFields(firstNameInput, lastNameInput, emailInput)
            val firstName = getString(firstNameInput)
            val lastName = getString(lastNameInput)
            val email = getValidEmail()

            firstName?.let {
                lastName?.let {
                    email?.let {
                        user = User(firstName, lastName, email)
                        user?.let {
                            UsersList.listOfUsers.add(it)
                            goBackToMainActivity()
                        }
                    }
                }
            }
        }
    }

    private fun goBackToMainActivity() {
        Intent(this, UsersActivity::class.java).also {
            setResult(RESULT_OK, it)
            it.putExtra("UPDATED_USER_INDEX", UsersList.listOfUsers.indexOf(user))
            startActivity(it)
        }
    }

    private fun getValidEmail(): String? {
        val input = emailInput.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            showToast("Type in valid Email Address")
            return null
        }
        return input
    }

    private fun validateFields(vararg viewElement: AppCompatEditText) {
        for (view in viewElement) {
            val text = view.text.toString()
            if (text.isEmpty()) {
                showToast("Fill all fields")
                break
            }
        }
    }

    private fun getString(viewElement: AppCompatEditText): String? {
        val input: String = viewElement.text.toString()
        return input.ifEmpty {
            null
        }
    }

    private fun showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}