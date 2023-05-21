package chordvers.lanstard.corn;

import chordvers.lanstard.KookVoiceChat;

public class deleteEmptyChannel implements Runnable{

    @Override
    public void run() {
        for(String s : KookVoiceChat.gamePlayerChatMap.getGroupList()){
            if(KookVoiceChat.gamePlayerChatMap.groupIsEmpty(s)){
                Channel.deleteVoiceChanel(s);
            }
        }
    }
}
