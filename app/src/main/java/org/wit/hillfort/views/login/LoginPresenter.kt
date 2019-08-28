package org.wit.hillfort.views.login

import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.generated.appcompatV7.R.id.info
import org.jetbrains.anko.toast
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class LoginPresenter(view: BaseView) : BasePresenter(view) {

  var auth: FirebaseAuth = FirebaseAuth.getInstance()
  var fireStore: HillfortFireStore? = null

  init {
    if (app.hillforts is HillfortFireStore) {
      fireStore = app.hillforts as HillfortFireStore
    }
  }

  fun doLogin(email: String, password: String) {
    view?.showProgress()
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        if (fireStore != null) {
          fireStore!!.fetchHillforts {
            view?.navigateTo(VIEW.LIST)
          }
        }
        else {
          view?.navigateTo(VIEW.LIST)
        }
      } else {
        view?.toast("Sign In Failed: ${task.exception?.message}")
      }
      view?.hideProgress()
    }
  }

  fun doSignUp(email: String, password: String) {
    view?.showProgress()
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        view?.navigateTo(VIEW.LIST)
      } else {
        view?.toast("Sign Up Failed: ${task.exception?.message}")
      }
      view?.hideProgress()
    }
  }
}