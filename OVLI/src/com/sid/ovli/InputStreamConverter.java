package com.sid.ovli;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;


 class InputStreamConverter extends InputStream{
 
        private final Reader input;				/* Le Reader utilis� pour lire le flux original */
        private final String outputCharset;		/* Le charset utilis� pour la sortie des donn�es */       
        private char[] cbuf = new char[4092];	/* Buffer de lecture du Reader */
        private byte[] data = null;				/* Buffer des donn�es avec le bon charset */
        int pos = 0;							/* Position dans le buffer de donn�es */
 
 
        /**
         * Cr�ation d'un InputStream � partir des donn�es d'un InputStream avec le charset par d�faut,
         * et en les encodant avec un autre charset.
         * @param input L(InputStream qui sera utilis�e pour lire les donn�es
         * @param outputCharset Le charset utilis�e pour encoder les donn�es lu via read().
         */
        public InputStreamConverter(InputStream input, String outputCharset) {
            this(new InputStreamReader(input), outputCharset);
        }
 
        /**
         * Cr�ation d'un InputStream � partir des donn�es d'un InputStream selon un charset sp�cifique,
         * et en les encodant avec un autre charset.
         * @param input L(InputStream qui sera utilis�e pour lire les donn�es
         * @param inputCharset Le charset utilis�e pour lire les donn�es.
         * @param outputCharset Le charset utilis�e pour encoder les donn�es lu via read().
         * @throws UnsupportedEncodingException
         */
        public InputStreamConverter(InputStream input, String inputCharset, String outputCharset) throws UnsupportedEncodingException {
            this(new InputStreamReader(input, inputCharset), outputCharset);
        }
 
        /**
         * Cr�ation d'un InputStream � partir des donn�es du Reader sp�cifi�,
         * en les encodant avec un autre charset.
         * @param reader Le Reader qui sera utilis�e pour lire les donn�es
         * @param charset Le charset utilis�e pour encoder les donn�es lu via read().
         */
        public InputStreamConverter(Reader reader, String charset) {
            this.input = reader;
            this.outputCharset = charset;
        }
 
        @Override
        public int read() throws IOException {
            if (data==null || pos>=data.length) {
 
                // lecture buff�ris�e depuis le Reader
                int len = input.read(cbuf);
                if (len>0) {
                    // on transforme le char[] en String
                    // afin de pouvoir le convertir en byte[] au format voulu :
                    data = new String(cbuf,0,len).getBytes(this.outputCharset);
                    // On r�initialise la position de lecture :
                    pos = 0;
                } else {
                    // on a atteint la fin du flux
                    data = null;
                }
            }
            if (data==null)
                return -1;
            // On renvoit les donn�es du buffer en tampon (si pr�sente).
            return data[pos++];
        }
 
        @Override
        public void close() throws IOException {
            this.input.close();
        }
 
        @Override
        protected void finalize() throws Throwable {
            this.input.close();
        }
    }
