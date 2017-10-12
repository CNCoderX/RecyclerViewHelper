package com.cncoderx.test.recyclerviewhelper.data;

import android.graphics.drawable.AnimationDrawable;

import com.cncoderx.test.recyclerviewhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/29.
 */
public class AlbumManager {

    public static List<Album> obtainAlbumList() {
        List<Album> albums = new ArrayList<>();
        albums.addAll(ALBUMS);
        return albums;
    }

    public static List<Album> obtainAlbumList(int start, int size) {
        List<Album> albums = new ArrayList<>();
        int end = Math.min(ALBUMS.size(), start + size);
        for (int i = start; i < end; i++) {
            albums.add(ALBUMS.get(i));
        }
        return albums;
    }

    public static List<Album> obtainAlbumList(String category) {
        List<Album> albums = new ArrayList<>();
        for (Album album : ALBUMS) {
            if (album.getCategory().equals(category))
                albums.add(album);
        }
        return albums;
    }

    static ArrayList<Album> ALBUMS = new ArrayList<>();
    static
    {
        {
            Album album = new Album();
            album.setCover(R.drawable.cover01);
            album.setName("Nicola Benedetti - Mendelssohn / MacMillan / Mozart");
            album.setPrice("$15.97");
            album.setCategory("classical");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover02);
            album.setName("Martha Argerich - Argerich Plays Chopin");
            album.setPrice("$15.97");
            album.setCategory("classical");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover03);
            album.setName("Alfred Brendel - Beethoven: Piano Concerto No.5 \"Emperor\" / Piano Sonata No.23 \"Appassionata\"");
            album.setPrice("$15.97");
            album.setCategory("classical");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover04);
            album.setName("Various Artists - Figure Skating's Greatest Hits");
            album.setPrice("$15.97");
            album.setCategory("classical");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover05);
            album.setName("Brett Eldredge - Brett Eldredge");
            album.setPrice("$12.88");
            album.setCategory("country");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover06);
            album.setName("Chris Stapleton - From A Room Vol. 1");
            album.setPrice("$10");
            album.setCategory("country");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover07);
            album.setName("Dean Brody - Beautiful Freakshow");
            album.setPrice("$12.88");
            album.setCategory("country");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover08);
            album.setName("Cinque - Catch A Corner");
            album.setPrice("$15.97");
            album.setCategory("jazz");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover09);
            album.setName("The Paul Desmond Quartet - Like Someone In Love");
            album.setPrice("$9.97");
            album.setCategory("jazz");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover10);
            album.setName("Count Basie - The Jazz Biography");
            album.setPrice("$5.97");
            album.setCategory("jazz");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover11);
            album.setName("The Chainsmokers - Memories… Do Not Open");
            album.setPrice("$12.88");
            album.setCategory("pop");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover12);
            album.setName("Taylor Swift - Fearless");
            album.setPrice("$10");
            album.setCategory("pop");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover13);
            album.setName("Sara Bareilles - The Blessed Unrest");
            album.setPrice("$8");
            album.setCategory("pop");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover14);
            album.setName("Miley Cyrus - The Time Of Our Lives (Walmart Exclusive)");
            album.setPrice("$8");
            album.setCategory("pop");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover15);
            album.setName("Little Mix - DNA");
            album.setPrice("$12.88");
            album.setCategory("pop");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover16);
            album.setName("The Summer Set - Everything's Fine");
            album.setPrice("$8.97");
            album.setCategory("pop");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover17);
            album.setName("Papa Roach - Crooked Teeth");
            album.setPrice("$12.88");
            album.setCategory("rock");
            ALBUMS.add(album);
        }
        {
            Album album = new Album();
            album.setCover(R.drawable.cover18);
            album.setName("Bruce Springsteen - Greatest Hits");
            album.setPrice("$6.97");
            album.setCategory("rock");
            ALBUMS.add(album);
        }
    }
}
