import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class Message {
    
    static boolean containsAny(String message, String[] keywords) {
        for (String word : keywords) {
            if (message.contains(word)) {
                return true;
            }
        }
        return false;
    }

    static String randomReply(String[] replies) {
        return replies[(int) (Math.random() * replies.length)];
    }

    public static void main(String[]args){


        //JFrame for the messagingi section
        JFrame frm = new JFrame();
        frm.setTitle("FLUX-WHISPER☺");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setResizable(false);
        frm.setSize(600,500);
        frm.setLayout(new BorderLayout());

        //change ImageIcon for the JFrame
        ImageIcon chat = new ImageIcon("C:\\Users\\Terix\\Documents\\MindMate\\chatpoint.png");
        frm.setIconImage(chat.getImage());
        frm.getContentPane().setBackground(new Color(0xE0E0E0));


        //Chat Area
        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setLineWrap(true);
        text.setBackground(new Color(0x1E0F26));
        text.setForeground(new Color(0xe6d9fc));
        JScrollPane scrollPane = new JScrollPane(text);

        //Input panel
        JPanel panel = new JPanel(new BorderLayout());
        JTextField input  = new JTextField();
        JButton button = new JButton("MESSAGE");

        input.setBackground(new Color(0x2E1437));
        input.setForeground(new Color(0xffffff));

        button.setForeground(new Color(0x1e0f26));
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(new Color(0xbb86fc));


        panel.add(input,BorderLayout.CENTER);
        panel.add(button,BorderLayout.EAST);

         String[] happyWords = {
                "happy", "excited", "great", "amazing", "fun","good", "joyful", "i feel loved","content","love","married","gave Birth","feel Blessed","sweet","delicious"
        };

        String[] sadWords = {
                "sad","am not feeling well","hopeless","sorrow","down", "tired", "lonely", "unhappy", "depressed","bored","am breaking","am low","life is not moving","things are falling apart"
        };

        String[] distressWords = {
                "stressed", "overwhelmed", "anxious", "am stranded","panic",
                "burnt out", "breaking down", "can't cope","despair"
        };

        String[] adviceWords = {
                "advice", "help me", "can you help", "what should i do",
                "can u advice me"
        };
         ActionListener sendAction = e -> {
            String userText = input.getText().trim();
            if (userText.isEmpty()) return;

            text.append("You: " + userText + "\n");
            String msg = userText.toLowerCase();
            String botReply;

            if (containsAny(msg, happyWords)) {
                String[] replies = {
                        "I'm really glad you're feeling happy 😊",
                        "That's wonderful to hear ♥♥",
                        "Keep enjoying the positive moments 💫"
                };
                botReply = randomReply(replies);
            }

            else if (containsAny(msg, distressWords)) {
                botReply = "That sounds overwhelming 😟 Take a slow breath. "
                        + "You're not weak for feeling this way.";
            }

            else if (containsAny(msg, sadWords)) {
                botReply = "I'm sorry you're feeling low 💙 "
                        + "Want to talk about what's making you feel this way?";
            }

            else if (containsAny(msg, adviceWords)) {
                botReply = "Of course  My advice: take things one step at a time. "
                        + "Rest, talk to someone you trust, and be kind to yourself.";
            }

            else if (msg.equals("hi") || msg.equals("hello")) {
                botReply = "Hello ,How are you feeling today?";
            }

            else if (msg.equals("bye")) {
                botReply = "Goodbye 👋 Remember, I'm always here if you need to talk.Glad i solved something 😊and feel welcomed to Talk to Flux-Whisper";
            }
            else if(msg.equals("how are you?")){
                botReply = "I am doing well, thank you for askingi feel blessed.I am here, calm and ready to listen.\n How are you feeling right now?";
            }
            else if(msg.equals("Will try to do that.")){
                botReply = "That sounds a very strong step remember never give up if you slip.";
            }
            else if(msg.equals("Do you think i can make it in life")){
                botReply = "Of Course you are a winner Surviving is always the key";                          
            }

            else {
                botReply = "I'm listening dear👂, Tell me more about how you're feeling;Life is all about communicating!!";
            }

            text.append("Bot: " + botReply + "\n\n");
            input.setText("");
        };


        // Button's ActionListener
        button.addActionListener(sendAction);

        // EnterKey
        input.addActionListener(sendAction);

       //JFrame's Visibility
       frm.add(scrollPane, BorderLayout.CENTER);
       frm.add(panel, BorderLayout.SOUTH);
      frm.setVisible(true);

    }
    
}
