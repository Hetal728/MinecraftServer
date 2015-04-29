package net.minecraft.util;

public interface IProgressUpdate {
   void displaySavingString(String message);

   void displayLoadingString(String message);

   void setLoadingProgress(int progress);
}
