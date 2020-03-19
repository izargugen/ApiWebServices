package com.sid.ovli;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;


 class InputStreamConverter extends InputStream{
 
        private final Reader input;				/* Le Reader utilisé pour lire le flux original */
        private final String outputCharset;		/* Le charset utilisé pour la sortie des données */       
        private char[] cbuf = new char[4092];	/* Buffer de lecture du Reader */
        private byte[] data = null;				/* Buffer des données avec le bon charset */
        int pos = 0;							/* Position dans le buffer de données */
 
 
        /**
         * Création d'un InputStream à partir des données d'un InputStream avec le charset par défaut,
         * et en les encodant avec un autre charset.
         * @param input L(InputStream qui sera utilisée pour lire les données
         * @param outputCharset Le charset utilisée pour encoder les données lu via read().
         */
        public InputStreamConverter(InputStream input, String outputCharset) {
            this(new InputStreamReader(input), outputCharset);
        }
 
        /**
         * Création d'un InputStream à partir des données d'un InputStream selon un charset spécifique,
         * et en les encodant avec un autre charset.
         * @param input L(InputStream qui sera utilisée pour lire les données
         * @param inputCharset Le charset utilisée pour lire les données.
         * @param outputCharset Le charset utilisée pour encoder les données lu via read().
         * @throws UnsupportedEncodingException
         */
        public InputStreamConverter(InputStream input, String inputCharset, String outputCharset) throws UnsupportedEncodingException {
            this(new InputStreamReader(input, inputCharset), outputCharset);
        }
 
        /**
         * Création d'un InputStream à partir des données du Reader spécifié,
         * en les encodant avec un autre charset.
         * @param reader Le Reader qui sera utilisée pour lire les données
         * @param charset Le charset utilisée pour encoder les données lu via read().
         */
        public InputStreamConverter(Reader reader, String charset) {
            this.input = reader;
            this.outputCharset = charset;
        }
 
        @Override
        public int read() throws IOException {
            if (data==null || pos>=data.length) {
 
                // lecture bufférisée depuis le Reader
                int len = input.read(cbuf);
                if (len>0) {
                    // on transforme le char[] en String
                    // afin de pouvoir le convertir en byte[] au format voulu :
                    data = new String(cbuf,0,len).getBytes(this.outputCharset);
                    // On réinitialise la position de lecture :
                    pos = 0;
                } else {
                    // on a atteint la fin du flux
                    data = null;
                }
            }
            if (data==null)
                return -1;
            // On renvoit les données du buffer en tampon (si présente).
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
