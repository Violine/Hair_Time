package com.smartcalendar.ilienkovkorovin.kalendar.Utils;

import android.content.Context;
import android.widget.Toast;

import com.smartcalendar.ilienkovkorovin.kalendar.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class ParseXML {
    public static ArrayList<String> getParsedXML(Context context) {
        ArrayList<String> allCity = new ArrayList<>();
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.rocid);

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("name")) {
                    allCity.add(parser.nextText());
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(context, context.getResources().getText(R.string.xml_read_error) + t.toString(), Toast.LENGTH_LONG).show();
        }
        return allCity;
    }
}

