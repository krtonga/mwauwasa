package com.github.codetanzania.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FAQEntry implements Parcelable {
    @SerializedName("qn")
    String question;

    @SerializedName("ans")
    String answer;

    public FAQEntry() {}

    protected FAQEntry(Parcel in) {
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<FAQEntry> CREATOR = new Creator<FAQEntry>() {
        @Override
        public FAQEntry createFromParcel(Parcel in) {
            return new FAQEntry(in);
        }

        @Override
        public FAQEntry[] newArray(int size) {
            return new FAQEntry[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }
}
