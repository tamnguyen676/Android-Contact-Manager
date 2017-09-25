#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>

#define MAX_LINE 80 /* The max length command */

int main(void)
{
  char *args[MAX_LINE/2 + 1]; //Maximum # of arguments 
  char history[100][21];
  char ss[MAX_LINE];
  char sg[MAX_LINE*2];
  char snum[5];
  int count;
  int should_run = 1; // Flag when to exit
  int i = 0; // argument count
  int j;
  int commandNum = 0;

  
  //Get History # Count
  FILE *fp = fopen("CMD_HISTORY.txt","r");
    char ch;
    count = 0;
  		while(!feof(fp))
  		{
  			ch = fgetc(fp);
  			if(ch == '\n')
   	 			count++;
  		}
  		fclose(fp);
  
            
  while(should_run)
  {
        printf("osh> ");
        fflush(stdout);
        
        scanf ("%[^\n]%*c", ss); //Parses commands with arguments
        printf("input:%s\n",ss);
        if(strcmp(ss,"exit") !=0 && strcmp(ss,"history") !=0)
        {
       	 count++;
       	 FILE *fp = fopen("CMD_HISTORY.txt","a");
       	 sprintf(snum, "%i ", count);
       	 strcpy(sg,snum);
       	 strcat(sg,ss);
       	 strcat(sg,"\n");
       	 fputs(sg, fp);
   		 fclose(fp);
        }
        i = 0; // argument count
        int j;
        args[i] = strtok(ss," "); //splits the string by " " and assigns to args[i]
       
       //Creates History Array
       if(strcmp(args[0], "!!") == 0 || strcmp(args[0], "history") == 0)
       {
        int count_d = 0;
      	FILE *fp = fopen("CMD_HISTORY.txt","r");
    	if ( fp != NULL )
 		  {
    		  char line [20]; /* or other suitable maximum line size */
      		  while ( fgets (line, sizeof line, fp ) != NULL ) /* read a line */
      		   {
       				  strcat(history[count_d],line);
       				  count_d++;
     		   }
  		 }
  		 fclose (fp);
        }
        if(strcmp(args[0], "!!") == 0 || strcmp(ss,"!!") ==0)
        {
        printf("Most recent command repeats \n");
        }
        //Prints Full History
        if(strcmp(args[0], "history") == 0 || strcmp(ss,"history") ==0)//exit command to stop running
        {
      	 for(int z = count; z>=0; z--)
      	 	printf("%s", history[z]);
      	 	
        }
        
        if((strcmp(args[0], "exit") == 0) || (strcmp(ss, "exit") == 0))//exit command to stop running
          return 0; 
          
        while (args[i] != NULL)  //loops until no more args left
        {
          i++; //increments arg count
          args[i] = strtok(NULL, " "); //splits the string by " " and assigns to args[i]
        }

        
        
        if(strcmp(args[i-1], "&") != 0) //if '&' at the end of args child and parent run concurrently 
        {
          pid_t pid; //Creates the parent process
          pid = fork(); //Creates the child
          if(pid < 0) //Negative Process Id's don't exist 
          {
                fprintf(stderr,"FORK Failed\n");
                return 1;
          }
          else if (pid == 0) //Child Process
          {
                execvp(args[0],args); //executes args[0] and args stores  parameters of args[0]

                for(int j=0;j<i;j++) //loop sets all args to NULL
                  args[j] = NULL;
          }
          else
          {
                wait(NULL); //parent waits for child to finish
          }
        }
        else
        {
          pid_t pid; //Creates the parent process
          pid = fork(); //Creates the child
          if(pid < 0) //Negative Process Id's don't exist 
          {
                fprintf(stderr,"FORK Failed\n");
                return 1;
          }
          else if (pid == 0) // Child Process
          {
                args[i-1] = NULL; //Sets last args to NULL
                execvp(args[0],args); //executes args[0] and args stores  parameters of args[0]
          }
          else
          {
                printf("\n\n");
          }
        }

  }
  return 0;
 
}