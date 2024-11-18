package com.myproyect.gestornovelasnjr.gestor_novelas.Novelas;

import android.os.Parcel;
import android.os.Parcelable;

public class Novel implements Parcelable {
    private String id;        // ID de la novela
    private String title;     // Título de la novela
    private String author;    // Autor de la novela
    private int year;         // Año de publicación
    private String synopsis;  // Sinopsis de la novela
    private boolean favorite; // Estado de favorito

    // **Constructor sin argumentos requerido por Firestore**
    public Novel() {
    }

    // Constructor completo
    public Novel(String id, String title, String author, int year, String synopsis, boolean favorite) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.synopsis = synopsis;
        this.favorite = favorite;
    }

    // Constructor utilizado al crear una nueva novela sin ID (el ID se generará)
    public Novel(String title, String author, int year, String synopsis) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.synopsis = synopsis;
        this.favorite = false;
    }

    // Implementación de Parcelable
    protected Novel(Parcel in) {
        id = in.readString();
        title = in.readString();
        author = in.readString();
        year = in.readInt();
        synopsis = in.readString();
        favorite = in.readByte() != 0;
    }

    public static final Creator<Novel> CREATOR = new Creator<Novel>() {
        @Override
        public Novel createFromParcel(Parcel in) {
            return new Novel(in);
        }

        @Override
        public Novel[] newArray(int size) {
            return new Novel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(year);
        dest.writeString(synopsis);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Resto de getters y setters...

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Similar para author, year, synopsis, favorite
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}

