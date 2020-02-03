package to.msn.wings.lenzeed.Inference;

import android.graphics.Bitmap;

public interface Classifier {

    class Recognition{
        private final String id;
        private final String title;
        /**
         *  モデルを量子化するかどうか
         * */
        private final boolean quant;
        /**
         * 認識スコア
         * */
        private final Float confidence;

        public Recognition(final String id, final String title, final Float confidence, final boolean quant){
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.quant = quant;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        /**
         * 認識結果をViewに反映するときに呼び出す関数
         * */
        @Override
        public String toString() {
            String resultString = "";
            if(id != null){
                resultString += "[" + id + "]";
            }

            if(title != null){
                resultString += title + "";
            }

            if(confidence != null){
                resultString += String.format("(%.1f%%)", confidence * 100.0f);
            }
            return resultString.trim();
        }
    }

    float[][] recognizeImage(Bitmap bitmap);

    void close();
}
