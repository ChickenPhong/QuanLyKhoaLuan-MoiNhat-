import { initializeApp } from "firebase/app";
import { getDatabase } from "firebase/database";
const firebaseConfig = {
  apiKey: "AIzaSyACXCoaw4mKmYrMnmemmfBTnvBiHzvGA48",
  authDomain: "quanlykhoaluan-969fa.firebaseapp.com",
  databaseURL: "https://quanlykhoaluan-969fa-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "quanlykhoaluan-969fa",
  storageBucket: "quanlykhoaluan-969fa.appspot.com",
  messagingSenderId: "202502812744",
  appId: "1:202502812744:web:0773ebc01b04f2400a547d",
  measurementId: "G-06KL0T374K"
};

const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

export { db };