package com.plt.yzplatform.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */

public class SubscribeTag {

    /**
     * data : {"result":[{"car_age_start":"","car_model":"","car_emissions_end":"5","car_mileage_end":"15","fuel_type_name":"柴油","type_name":"","car_gearbox":"YZcarscreenbsxsd","letout_name":"","city":"jinan","car_letout":"","brand_name":"保时捷","car_fuel_type":"YZcarscreenoilcy","car_type":"","city_name":"济南","car_seating":"YZcarscreenseatfour","geatbox_name":"手动","model_name":"","seating_name":"4座","car_age_end":"","car_brand":"YZescxgescppcxdlxlcnkcxmy_40","car_mileage_start":"0","car_emissions_start":"0"},{"car_age_start":"","car_model":"","car_emissions_end":"","car_mileage_end":"","fuel_type_name":"","type_name":"","car_gearbox":"","letout_name":"","city":"jinan","car_letout":"","brand_name":"","car_fuel_type":"","car_type":"","city_name":"济南","car_seating":"","geatbox_name":"","model_name":"","seating_name":"","car_age_end":"","car_brand":"","car_mileage_start":"","car_emissions_start":""}]}
     * message :
     * status : 1
     */

    private DataBean data;
    private String message;
    private String status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class DataBean {
        private List<ResultBean> result;

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean implements Parcelable{
            /**
             * car_subs_id
             * car_age_start :
             * car_model :
             * car_emissions_end : 5
             * car_mileage_end : 15
             * fuel_type_name : 柴油
             * type_name :
             * car_gearbox : YZcarscreenbsxsd
             * letout_name :
             * city : jinan
             * car_letout :
             * brand_name : 保时捷
             * car_fuel_type : YZcarscreenoilcy
             * car_type :
             * city_name : 济南
             * car_seating : YZcarscreenseatfour
             * geatbox_name : 手动
             * model_name :
             * seating_name : 4座
             * car_age_end :
             * car_brand : YZescxgescppcxdlxlcnkcxmy_40
             * car_mileage_start : 0
             * car_emissions_start : 0
             */

            private String car_age_start;
            private String car_model;
            private String car_emissions_end;
            private String car_mileage_end;
            private String fuel_type_name;
            private String type_name;
            private String car_gearbox;
            private String letout_name;
            private String car_city;
            private String car_letout;
            private String brand_name;
            private String car_fuel_type;
            private String car_type;
            private String city_name;
            private String car_seating;
            private String gearbox_name;
            private String model_name;
            private String seating_name;
            private String car_age_end;
            private String car_brand;
            private String car_mileage_start;
            private String car_emissions_start;
            private String car_subs_id;

            protected ResultBean(Parcel in) {
                car_age_start = in.readString();
                car_model = in.readString();
                car_emissions_end = in.readString();
                car_mileage_end = in.readString();
                fuel_type_name = in.readString();
                type_name = in.readString();
                car_gearbox = in.readString();
                letout_name = in.readString();
                car_city = in.readString();
                car_letout = in.readString();
                brand_name = in.readString();
                car_fuel_type = in.readString();
                car_type = in.readString();
                city_name = in.readString();
                car_seating = in.readString();
                gearbox_name = in.readString();
                model_name = in.readString();
                seating_name = in.readString();
                car_age_end = in.readString();
                car_brand = in.readString();
                car_mileage_start = in.readString();
                car_emissions_start = in.readString();
                car_subs_id = in.readString();
            }

            public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
                @Override
                public ResultBean createFromParcel(Parcel in) {
                    return new ResultBean(in);
                }

                @Override
                public ResultBean[] newArray(int size) {
                    return new ResultBean[size];
                }
            };

            public String getCar_subs_id() {
                return car_subs_id;
            }

            public void setCar_subs_id(String car_subs_id) {
                this.car_subs_id = car_subs_id;
            }



            public String getCar_age_start() {
                return car_age_start;
            }

            public void setCar_age_start(String car_age_start) {
                this.car_age_start = car_age_start;
            }

            public String getCar_model() {
                return car_model;
            }

            public void setCar_model(String car_model) {
                this.car_model = car_model;
            }

            public String getCar_emissions_end() {
                return car_emissions_end;
            }

            public void setCar_emissions_end(String car_emissions_end) {
                this.car_emissions_end = car_emissions_end;
            }

            public String getCar_mileage_end() {
                return car_mileage_end;
            }

            public void setCar_mileage_end(String car_mileage_end) {
                this.car_mileage_end = car_mileage_end;
            }

            public String getFuel_type_name() {
                return fuel_type_name;
            }

            public void setFuel_type_name(String fuel_type_name) {
                this.fuel_type_name = fuel_type_name;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getCar_gearbox() {
                return car_gearbox;
            }

            public void setCar_gearbox(String car_gearbox) {
                this.car_gearbox = car_gearbox;
            }

            public String getLetout_name() {
                return letout_name;
            }

            public void setLetout_name(String letout_name) {
                this.letout_name = letout_name;
            }

            public String getCity() {
                return car_city;
            }

            public void setCity(String city) {
                this.car_city = city;
            }

            public String getCar_letout() {
                return car_letout;
            }

            public void setCar_letout(String car_letout) {
                this.car_letout = car_letout;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getCar_fuel_type() {
                return car_fuel_type;
            }

            public void setCar_fuel_type(String car_fuel_type) {
                this.car_fuel_type = car_fuel_type;
            }

            public String getCar_type() {
                return car_type;
            }

            public void setCar_type(String car_type) {
                this.car_type = car_type;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getCar_seating() {
                return car_seating;
            }

            public void setCar_seating(String car_seating) {
                this.car_seating = car_seating;
            }

            public String getGearbox_name() {
                return gearbox_name;
            }

            public void setGearbox_name(String gearbox_name) {
                this.gearbox_name = gearbox_name;
            }

            public String getModel_name() {
                return model_name;
            }

            public void setModel_name(String model_name) {
                this.model_name = model_name;
            }

            public String getSeating_name() {
                return seating_name;
            }

            public void setSeating_name(String seating_name) {
                this.seating_name = seating_name;
            }

            public String getCar_age_end() {
                return car_age_end;
            }

            public void setCar_age_end(String car_age_end) {
                this.car_age_end = car_age_end;
            }

            public String getCar_brand() {
                return car_brand;
            }

            public void setCar_brand(String car_brand) {
                this.car_brand = car_brand;
            }

            public String getCar_mileage_start() {
                return car_mileage_start;
            }

            public void setCar_mileage_start(String car_mileage_start) {
                this.car_mileage_start = car_mileage_start;
            }

            public String getCar_emissions_start() {
                return car_emissions_start;
            }

            public void setCar_emissions_start(String car_emissions_start) {
                this.car_emissions_start = car_emissions_start;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(car_age_start);
                dest.writeString(car_model);
                dest.writeString(car_emissions_end);
                dest.writeString(car_mileage_end);
                dest.writeString(fuel_type_name);
                dest.writeString(type_name);
                dest.writeString(car_gearbox);
                dest.writeString(letout_name);
                dest.writeString(car_city);
                dest.writeString(car_letout);
                dest.writeString(brand_name);
                dest.writeString(car_fuel_type);
                dest.writeString(car_type);
                dest.writeString(city_name);
                dest.writeString(car_seating);
                dest.writeString(gearbox_name);
                dest.writeString(model_name);
                dest.writeString(seating_name);
                dest.writeString(car_age_end);
                dest.writeString(car_brand);
                dest.writeString(car_mileage_start);
                dest.writeString(car_emissions_start);
                dest.writeString(car_subs_id);
            }
        }
    }
}
