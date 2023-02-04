package com.example.mensajeriasockets.extra;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
    //  Esta clase se encarga de proprocionar espacio entre cada celda del RecyclerView de los mensajes.
public class SpacingDecorator extends RecyclerView.ItemDecoration{
    private int verticalSpace;
    public SpacingDecorator(int verticalSpace){
        this.verticalSpace = verticalSpace;
    }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.bottom = verticalSpace; //  Se aplica el espaciado debajo de la celda.
        }
    }
