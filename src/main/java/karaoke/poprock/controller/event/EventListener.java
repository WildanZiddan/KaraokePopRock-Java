package karaoke.poprock.controller.event;

import javafx.event.ActionEvent;
//import himma.pendidikan.controller.KaryawanCtrl;

public abstract class EventListener {
    //private static KaryawanCtrl kry = new KaryawanCtrl();

    public abstract void handleClear();


    public static abstract class EventListenerIndex extends EventListener {
        public abstract void handleSearch();
    }


    public static abstract class EventListenerCreate extends EventListener{
        public abstract void handleAddData(ActionEvent e);

        public void handleBack(){
            //kry.loadSubPage("index",null);
        };
    }


    public static abstract class EventListenerUpdate extends EventListener{
        public abstract void handleUpdateData(ActionEvent e);

        public abstract void loadData();

        public void handleBack(){
            //kry.loadSubPage("index",null);
        };
    }
}
