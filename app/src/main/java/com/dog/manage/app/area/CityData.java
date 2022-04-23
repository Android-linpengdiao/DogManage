package com.dog.manage.app.area;


import com.base.utils.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityData {

    /**
     * id : 13
     * name : 河北省
     * children : {"1301":{"id":"1301","name":"石家庄市","parentId":"13","children":{"130102":{"id":"130102","name":"长安区","parentId":"1301"},"130104":{"id":"130104","name":"桥西区","parentId":"1301"},"130105":{"id":"130105","name":"新华区","parentId":"1301"},"130107":{"id":"130107","name":"井陉矿区","parentId":"1301"},"130108":{"id":"130108","name":"裕华区","parentId":"1301"},"130109":{"id":"130109","name":"藁城区","parentId":"1301"},"130110":{"id":"130110","name":"鹿泉区","parentId":"1301"},"130111":{"id":"130111","name":"栾城区","parentId":"1301"},"130121":{"id":"130121","name":"井陉县","parentId":"1301"},"130123":{"id":"130123","name":"正定县","parentId":"1301"},"130125":{"id":"130125","name":"行唐县","parentId":"1301"},"130126":{"id":"130126","name":"灵寿县","parentId":"1301"},"130127":{"id":"130127","name":"高邑县","parentId":"1301"},"130128":{"id":"130128","name":"深泽县","parentId":"1301"},"130129":{"id":"130129","name":"赞皇县","parentId":"1301"},"130130":{"id":"130130","name":"无极县","parentId":"1301"},"130131":{"id":"130131","name":"平山县","parentId":"1301"},"130132":{"id":"130132","name":"元氏县","parentId":"1301"},"130133":{"id":"130133","name":"赵县","parentId":"1301"},"130171":{"id":"130171","name":"石家庄高新技术产业开发区","parentId":"1301"},"130172":{"id":"130172","name":"石家庄循环化工园区","parentId":"1301"},"130181":{"id":"130181","name":"辛集市","parentId":"1301"},"130183":{"id":"130183","name":"晋州市","parentId":"1301"},"130184":{"id":"130184","name":"新乐市","parentId":"1301"}}},"1302":{"id":"1302","name":"唐山市","parentId":"13","children":{"130202":{"id":"130202","name":"路南区","parentId":"1302"},"130203":{"id":"130203","name":"路北区","parentId":"1302"},"130204":{"id":"130204","name":"古冶区","parentId":"1302"},"130205":{"id":"130205","name":"开平区","parentId":"1302"},"130207":{"id":"130207","name":"丰南区","parentId":"1302"},"130208":{"id":"130208","name":"丰润区","parentId":"1302"},"130209":{"id":"130209","name":"曹妃甸区","parentId":"1302"},"130224":{"id":"130224","name":"滦南县","parentId":"1302"},"130225":{"id":"130225","name":"乐亭县","parentId":"1302"},"130227":{"id":"130227","name":"迁西县","parentId":"1302"},"130229":{"id":"130229","name":"玉田县","parentId":"1302"},"130271":{"id":"130271","name":"河北唐山芦台经济开发区","parentId":"1302"},"130272":{"id":"130272","name":"唐山市汉沽管理区","parentId":"1302"},"130273":{"id":"130273","name":"唐山高新技术产业开发区","parentId":"1302"},"130274":{"id":"130274","name":"河北唐山海港经济开发区","parentId":"1302"},"130281":{"id":"130281","name":"遵化市","parentId":"1302"},"130283":{"id":"130283","name":"迁安市","parentId":"1302"},"130284":{"id":"130284","name":"滦州市","parentId":"1302"}}},"1303":{"id":"1303","name":"秦皇岛市","parentId":"13","children":{"130302":{"id":"130302","name":"海港区","parentId":"1303"},"130303":{"id":"130303","name":"山海关区","parentId":"1303"},"130304":{"id":"130304","name":"北戴河区","parentId":"1303"},"130306":{"id":"130306","name":"抚宁区","parentId":"1303"},"130321":{"id":"130321","name":"青龙满族自治县","parentId":"1303"},"130322":{"id":"130322","name":"昌黎县","parentId":"1303"},"130324":{"id":"130324","name":"卢龙县","parentId":"1303"},"130371":{"id":"130371","name":"秦皇岛市经济技术开发区","parentId":"1303"},"130372":{"id":"130372","name":"北戴河新区","parentId":"1303"}}},"1304":{"id":"1304","name":"邯郸市","parentId":"13","children":{"130402":{"id":"130402","name":"邯山区","parentId":"1304"},"130403":{"id":"130403","name":"丛台区","parentId":"1304"},"130404":{"id":"130404","name":"复兴区","parentId":"1304"},"130406":{"id":"130406","name":"峰峰矿区","parentId":"1304"},"130407":{"id":"130407","name":"肥乡区","parentId":"1304"},"130408":{"id":"130408","name":"永年区","parentId":"1304"},"130423":{"id":"130423","name":"临漳县","parentId":"1304"},"130424":{"id":"130424","name":"成安县","parentId":"1304"},"130425":{"id":"130425","name":"大名县","parentId":"1304"},"130426":{"id":"130426","name":"涉县","parentId":"1304"},"130427":{"id":"130427","name":"磁县","parentId":"1304"},"130430":{"id":"130430","name":"邱县","parentId":"1304"},"130431":{"id":"130431","name":"鸡泽县","parentId":"1304"},"130432":{"id":"130432","name":"广平县","parentId":"1304"},"130433":{"id":"130433","name":"馆陶县","parentId":"1304"},"130434":{"id":"130434","name":"魏县","parentId":"1304"},"130435":{"id":"130435","name":"曲周县","parentId":"1304"},"130471":{"id":"130471","name":"邯郸经济技术开发区","parentId":"1304"},"130473":{"id":"130473","name":"邯郸冀南新区","parentId":"1304"},"130481":{"id":"130481","name":"武安市","parentId":"1304"}}},"1305":{"id":"1305","name":"邢台市","parentId":"13","children":{"130502":{"id":"130502","name":"桥东区","parentId":"1305"},"130503":{"id":"130503","name":"桥西区","parentId":"1305"},"130521":{"id":"130521","name":"邢台县","parentId":"1305"},"130522":{"id":"130522","name":"临城县","parentId":"1305"},"130523":{"id":"130523","name":"内丘县","parentId":"1305"},"130524":{"id":"130524","name":"柏乡县","parentId":"1305"},"130525":{"id":"130525","name":"隆尧县","parentId":"1305"},"130526":{"id":"130526","name":"任县","parentId":"1305"},"130527":{"id":"130527","name":"南和县","parentId":"1305"},"130528":{"id":"130528","name":"宁晋县","parentId":"1305"},"130529":{"id":"130529","name":"巨鹿县","parentId":"1305"},"130530":{"id":"130530","name":"新河县","parentId":"1305"},"130531":{"id":"130531","name":"广宗县","parentId":"1305"},"130532":{"id":"130532","name":"平乡县","parentId":"1305"},"130533":{"id":"130533","name":"威县","parentId":"1305"},"130534":{"id":"130534","name":"清河县","parentId":"1305"},"130535":{"id":"130535","name":"临西县","parentId":"1305"},"130571":{"id":"130571","name":"河北邢台经济开发区","parentId":"1305"},"130581":{"id":"130581","name":"南宫市","parentId":"1305"},"130582":{"id":"130582","name":"沙河市","parentId":"1305"}}},"1306":{"id":"1306","name":"保定市","parentId":"13","children":{"130602":{"id":"130602","name":"竞秀区","parentId":"1306"},"130606":{"id":"130606","name":"莲池区","parentId":"1306"},"130607":{"id":"130607","name":"满城区","parentId":"1306"},"130608":{"id":"130608","name":"清苑区","parentId":"1306"},"130609":{"id":"130609","name":"徐水区","parentId":"1306"},"130623":{"id":"130623","name":"涞水县","parentId":"1306"},"130624":{"id":"130624","name":"阜平县","parentId":"1306"},"130626":{"id":"130626","name":"定兴县","parentId":"1306"},"130627":{"id":"130627","name":"唐县","parentId":"1306"},"130628":{"id":"130628","name":"高阳县","parentId":"1306"},"130629":{"id":"130629","name":"容城县","parentId":"1306"},"130630":{"id":"130630","name":"涞源县","parentId":"1306"},"130631":{"id":"130631","name":"望都县","parentId":"1306"},"130632":{"id":"130632","name":"安新县","parentId":"1306"},"130633":{"id":"130633","name":"易县","parentId":"1306"},"130634":{"id":"130634","name":"曲阳县","parentId":"1306"},"130635":{"id":"130635","name":"蠡县","parentId":"1306"},"130636":{"id":"130636","name":"顺平县","parentId":"1306"},"130637":{"id":"130637","name":"博野县","parentId":"1306"},"130638":{"id":"130638","name":"雄县","parentId":"1306"},"130671":{"id":"130671","name":"保定高新技术产业开发区","parentId":"1306"},"130672":{"id":"130672","name":"保定白沟新城","parentId":"1306"},"130681":{"id":"130681","name":"涿州市","parentId":"1306"},"130682":{"id":"130682","name":"定州市","parentId":"1306"},"130683":{"id":"130683","name":"安国市","parentId":"1306"},"130684":{"id":"130684","name":"高碑店市","parentId":"1306"}}},"1307":{"id":"1307","name":"张家口市","parentId":"13","children":{"130702":{"id":"130702","name":"桥东区","parentId":"1307"},"130703":{"id":"130703","name":"桥西区","parentId":"1307"},"130705":{"id":"130705","name":"宣化区","parentId":"1307"},"130706":{"id":"130706","name":"下花园区","parentId":"1307"},"130708":{"id":"130708","name":"万全区","parentId":"1307"},"130709":{"id":"130709","name":"崇礼区","parentId":"1307"},"130722":{"id":"130722","name":"张北县","parentId":"1307"},"130723":{"id":"130723","name":"康保县","parentId":"1307"},"130724":{"id":"130724","name":"沽源县","parentId":"1307"},"130725":{"id":"130725","name":"尚义县","parentId":"1307"},"130726":{"id":"130726","name":"蔚县","parentId":"1307"},"130727":{"id":"130727","name":"阳原县","parentId":"1307"},"130728":{"id":"130728","name":"怀安县","parentId":"1307"},"130730":{"id":"130730","name":"怀来县","parentId":"1307"},"130731":{"id":"130731","name":"涿鹿县","parentId":"1307"},"130732":{"id":"130732","name":"赤城县","parentId":"1307"},"130771":{"id":"130771","name":"张家口经济开发区","parentId":"1307"},"130772":{"id":"130772","name":"张家口市察北管理区","parentId":"1307"},"130773":{"id":"130773","name":"张家口市塞北管理区","parentId":"1307"}}},"1308":{"id":"1308","name":"承德市","parentId":"13","children":{"130802":{"id":"130802","name":"双桥区","parentId":"1308"},"130803":{"id":"130803","name":"双滦区","parentId":"1308"},"130804":{"id":"130804","name":"鹰手营子矿区","parentId":"1308"},"130821":{"id":"130821","name":"承德县","parentId":"1308"},"130822":{"id":"130822","name":"兴隆县","parentId":"1308"},"130824":{"id":"130824","name":"滦平县","parentId":"1308"},"130825":{"id":"130825","name":"隆化县","parentId":"1308"},"130826":{"id":"130826","name":"丰宁满族自治县","parentId":"1308"},"130827":{"id":"130827","name":"宽城满族自治县","parentId":"1308"},"130828":{"id":"130828","name":"围场满族蒙古族自治县","parentId":"1308"},"130871":{"id":"130871","name":"承德高新技术产业开发区","parentId":"1308"},"130881":{"id":"130881","name":"平泉市","parentId":"1308"}}},"1309":{"id":"1309","name":"沧州市","parentId":"13","children":{"130902":{"id":"130902","name":"新华区","parentId":"1309"},"130903":{"id":"130903","name":"运河区","parentId":"1309"},"130921":{"id":"130921","name":"沧县","parentId":"1309"},"130922":{"id":"130922","name":"青县","parentId":"1309"},"130923":{"id":"130923","name":"东光县","parentId":"1309"},"130924":{"id":"130924","name":"海兴县","parentId":"1309"},"130925":{"id":"130925","name":"盐山县","parentId":"1309"},"130926":{"id":"130926","name":"肃宁县","parentId":"1309"},"130927":{"id":"130927","name":"南皮县","parentId":"1309"},"130928":{"id":"130928","name":"吴桥县","parentId":"1309"},"130929":{"id":"130929","name":"献县","parentId":"1309"},"130930":{"id":"130930","name":"孟村回族自治县","parentId":"1309"},"130971":{"id":"130971","name":"河北沧州经济开发区","parentId":"1309"},"130972":{"id":"130972","name":"沧州高新技术产业开发区","parentId":"1309"},"130973":{"id":"130973","name":"沧州渤海新区","parentId":"1309"},"130981":{"id":"130981","name":"泊头市","parentId":"1309"},"130982":{"id":"130982","name":"任丘市","parentId":"1309"},"130983":{"id":"130983","name":"黄骅市","parentId":"1309"},"130984":{"id":"130984","name":"河间市","parentId":"1309"}}},"1310":{"id":"1310","name":"廊坊市","parentId":"13","children":{"131002":{"id":"131002","name":"安次区","parentId":"1310"},"131003":{"id":"131003","name":"广阳区","parentId":"1310"},"131022":{"id":"131022","name":"固安县","parentId":"1310"},"131023":{"id":"131023","name":"永清县","parentId":"1310"},"131024":{"id":"131024","name":"香河县","parentId":"1310"},"131025":{"id":"131025","name":"大城县","parentId":"1310"},"131026":{"id":"131026","name":"文安县","parentId":"1310"},"131028":{"id":"131028","name":"大厂回族自治县","parentId":"1310"},"131071":{"id":"131071","name":"廊坊经济技术开发区","parentId":"1310"},"131081":{"id":"131081","name":"霸州市","parentId":"1310"},"131082":{"id":"131082","name":"三河市","parentId":"1310"}}},"1311":{"id":"1311","name":"衡水市","parentId":"13","children":{"131102":{"id":"131102","name":"桃城区","parentId":"1311"},"131103":{"id":"131103","name":"冀州区","parentId":"1311"},"131121":{"id":"131121","name":"枣强县","parentId":"1311"},"131122":{"id":"131122","name":"武邑县","parentId":"1311"},"131123":{"id":"131123","name":"武强县","parentId":"1311"},"131124":{"id":"131124","name":"饶阳县","parentId":"1311"},"131125":{"id":"131125","name":"安平县","parentId":"1311"},"131126":{"id":"131126","name":"故城县","parentId":"1311"},"131127":{"id":"131127","name":"景县","parentId":"1311"},"131128":{"id":"131128","name":"阜城县","parentId":"1311"},"131171":{"id":"131171","name":"河北衡水高新技术产业开发区","parentId":"1311"},"131172":{"id":"131172","name":"衡水滨湖新区","parentId":"1311"},"131182":{"id":"131182","name":"深州市","parentId":"1311"}}}}
     */

    private String id;
    private String name;
    private int status = 0;
    private List<FirstChildrenBean> children;

    public static CityData parseJsonToBean(String json) {
        CityData cityData = new CityData();
        try {
            JSONObject object = new JSONObject(json);
            cityData.setId(object.optString("id"));
            cityData.setName(object.optString("name"));
            JSONObject firstChildrenJson = object.optJSONObject("children");
            int sizeId = object.optInt("id") * 100;

            List<FirstChildrenBean> firstChilds = new ArrayList<>();
            for (int i = 1; i <= firstChildrenJson.length() + 50; i++) {
                int newsizeId = sizeId + i;
                JSONObject firstChildrenObject = firstChildrenJson.optJSONObject(String.valueOf(newsizeId));
                CityData.FirstChildrenBean firstChildrenBean = new CityData.FirstChildrenBean();
                if (!CommonUtil.isBlank(firstChildrenObject)) {
                    firstChildrenBean.setId(firstChildrenObject.optString("id"));
                    firstChildrenBean.setName(firstChildrenObject.optString("name"));
                    firstChildrenBean.setParentId(firstChildrenObject.optString("parentId"));
                    JSONObject secondChildrenJson = firstChildrenObject.optJSONObject("children");
                    int sizeID = firstChildrenObject.optInt("id") * 100;
                    List<FirstChildrenBean.SecondChildrenBean> secondChilds = new ArrayList<>();
                    for (int j = 1; j <= secondChildrenJson.length() + 50; j++) {
                        int newSizeID = sizeID + j;
                        JSONObject secondChildrenObject = secondChildrenJson.optJSONObject(String.valueOf(newSizeID));
                        if (!CommonUtil.isBlank(secondChildrenObject)) {
                            CityData.FirstChildrenBean.SecondChildrenBean secondChildrenBean = new CityData.FirstChildrenBean.SecondChildrenBean();
                            secondChildrenBean.setId(secondChildrenObject.optString("id"));
                            secondChildrenBean.setName(secondChildrenObject.optString("name"));
                            secondChildrenBean.setParentId(secondChildrenObject.optString("parentId"));
                            secondChilds.add(secondChildrenBean);
                        }
                    }
                    firstChildrenBean.setChildren(secondChilds);
                    firstChilds.add(firstChildrenBean);
                }
            }
            cityData.setChildren(firstChilds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FirstChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<FirstChildrenBean> children) {
        this.children = children;
    }

    public static class FirstChildrenBean {

        /**
         * id : 1301
         * name : 石家庄市
         * parentId : 13
         * children : {"130102":{"id":"130102","name":"长安区","parentId":"1301"},"130104":{"id":"130104","name":"桥西区","parentId":"1301"},"130105":{"id":"130105","name":"新华区","parentId":"1301"},"130107":{"id":"130107","name":"井陉矿区","parentId":"1301"},"130108":{"id":"130108","name":"裕华区","parentId":"1301"},"130109":{"id":"130109","name":"藁城区","parentId":"1301"},"130110":{"id":"130110","name":"鹿泉区","parentId":"1301"},"130111":{"id":"130111","name":"栾城区","parentId":"1301"},"130121":{"id":"130121","name":"井陉县","parentId":"1301"},"130123":{"id":"130123","name":"正定县","parentId":"1301"},"130125":{"id":"130125","name":"行唐县","parentId":"1301"},"130126":{"id":"130126","name":"灵寿县","parentId":"1301"},"130127":{"id":"130127","name":"高邑县","parentId":"1301"},"130128":{"id":"130128","name":"深泽县","parentId":"1301"},"130129":{"id":"130129","name":"赞皇县","parentId":"1301"},"130130":{"id":"130130","name":"无极县","parentId":"1301"},"130131":{"id":"130131","name":"平山县","parentId":"1301"},"130132":{"id":"130132","name":"元氏县","parentId":"1301"},"130133":{"id":"130133","name":"赵县","parentId":"1301"},"130171":{"id":"130171","name":"石家庄高新技术产业开发区","parentId":"1301"},"130172":{"id":"130172","name":"石家庄循环化工园区","parentId":"1301"},"130181":{"id":"130181","name":"辛集市","parentId":"1301"},"130183":{"id":"130183","name":"晋州市","parentId":"1301"},"130184":{"id":"130184","name":"新乐市","parentId":"1301"}}
         */

        private String id;
        private String name;
        private String parentId;
        private int status = 0;
        private List<SecondChildrenBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<SecondChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<SecondChildrenBean> children) {
            this.children = children;
        }

        public static class SecondChildrenBean {
            /**
             * id : 130102
             * name : 长安区
             * parentId : 1301
             */

            private String id;
            private String name;
            private String parentId;
            private int status = 0;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
