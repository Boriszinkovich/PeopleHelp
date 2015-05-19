package com.example.boris.androidproject;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.boris.androidproject.database.RequestDao;
import com.example.boris.androidproject.request.SosRequest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testDataBase()
    {
        /*ParseObject gameScore = new ParseObject("GameScore2");
        gameScore.put("score", 1337);
        gameScore.put("playerName", "Sean Plott");
        gameScore.put("cheatMode", false);
        String id = gameScore.getObjectId();
        gameScore.saveInBackground();*/
        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore2");
        query.whereEqualTo("playerName", "Sean Plott");
        List<ParseObject> list = new ArrayList<ParseObject>();
        try {
         list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("initialized", "Ura" + list.size());
        Log.d("initialized","URA"+list.get(0).getInt("score"));
        Assert.assertEquals(list.get(0).getInt("score"),1337);*/
        RequestDao.createUserRequest(new SosRequest("sdfdhh"),""+50,""+60);
        //HelpRequest bb = new HelpRequest(false,false,"sdfs","dfsdf","wefwe");
       // RequestDao.createUserRequest(bb,""+82,""+120,22734);
     //   List<RequestInterface> list = RequestDao.getAllRequests();
      //  Assert.assertEquals(list.size(),5);
       // Assert.assertEquals(list.get(3).getLongitude(),22334+"");
       // Assert.assertEquals(list.get(3).getRequestType(),22334+"");
      /*  Log.e("Basezz","s"+list.size());
        for (RequestInterface sa: list)
        {
            if (sa instanceof SosRequest) Log.e("Basezz","sos");
            if (sa instanceof HelpRequest) Log.e("Basezz","help");
        }*/
       // Assert.assertEquals(list.get(0).getThem(),"sdfds");
       // Assert.assertEquals();
    }
}