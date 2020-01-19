#include <stdio.h>
#include <stdlib.h>

typedef struct
{
    int id, pj1, pj2;
    char firstName[15];
    char surName[15];
    float grade;
} student;

student **create_class_list(char *filename,int *sizePtr);
int find(int idNo,student **list,int size);
void input_grades(char *filename,student **list,int size);
void compute_final_course_grades(student **list,int size);
void output_final_course_grades(char *filename,student **list,int size);
void print_list(student **list,int size);
void withdraw(int idNo,student **list,int *sizePtr);
void destroy_list(student **list,int *sizePtr);

int main()
{
    int sizefile=0;//initial value will be changed when file is read
    int loop=1,control=-1;//default values for menu controlling
    int id=0;//hold students id for multiple functions
    char mainFile[200];//name of main file(id and name) should not be to long, so 200 is a good initial value
    char gradeFile[200];//name of file that contains students grades
    char finals[200];//name of final grade file that is to be created


    printf("Enter file name(with .txt extention):\n");
    scanf("%s",mainFile);
    student**classList=create_class_list(mainFile,&sizefile);

    do
    {
        printf("\nMENU\n-------------------------------------------------\n");
        printf("1.Find student\n");
        printf("2.Input grades\n");
        printf("3.Compute final\n");
        printf("4.Output final\n");
        printf("5.Print class list\n");
        printf("6.Withdraw student\n");
        printf("7.Delete class list\n");
        printf("0.Exit\n");
        printf("--------------------------------------------------\n\n");

        printf("Enter option:\n");
        scanf("%d",&control);

        switch(control)
        {
            case 0:
                printf("Exiting.\n");
                loop=0;//cause to exit out of while loop
                break;

            case 1:
                printf("Enter student ID:\n");
                scanf("%d",&id);

                if(find(id,classList,sizefile)==-1)
                {
                    printf("\nStudent cannot be found\n\n");
                }
                else
                {
                    printf("\nThe student can be found at index %d\n\n",find(id,classList,sizefile));
                }
                break;

            case 2:
                printf("Enter the name of grade file(with .txt extenstion)\n");
                scanf("%s",gradeFile);

                input_grades(gradeFile,classList,sizefile);
                printf("\nGrades successfully uploaded.\n\n");
                break;

            case 3:
                compute_final_course_grades(classList,sizefile);
                printf("\nFinal grades successfully computed.\n\n");
                break;

            case 4:
                printf("Enter name of file you would like to create(with .txt extenstion):\n");
                scanf("%s",finals);
                output_final_course_grades(finals,classList,sizefile);
                printf("\nFile successfully created.\n\n");
                break;

            case 5:
                if(sizefile==0)
                {
                    printf("\nClass does not exist\n\n");
                }
                else
                {
                printf("\nClass list:\n");
                print_list(classList,sizefile);
                }
                break;

            case 6:
                printf("Remove student(enter ID):\n");
                scanf("%d",&id);

                withdraw(id,classList,&sizefile);
                break;

            case 7:
                destroy_list(classList,&sizefile);
                printf("\nClass successfully deleted.\n\n");
                break;

            default:
                printf("Please select one of the options\n");
                break;
        }

    }while(loop==1);

    return 0;
}// end of main

student **create_class_list(char *filename,int *sizePtr)
{
    FILE *file=fopen(filename,"r"); // open file
    fscanf(file,"%d\n",sizePtr);// read the amount of students then assigns to var sizefile in main

    student **class_list=malloc((*sizePtr)*sizeof(student*));//allocates memory for each student pointer
    for(int i=0;i<*sizePtr;i++)
    {
        class_list[i]=calloc(6,sizeof(student));//allocates memory for each student
    }// end of for

    for(int i=0;i<*sizePtr;i++)
    {
        fscanf(file,"%d%s%s\n",&(class_list[i]->id),class_list[i]->firstName,class_list[i]->surName);
    }//end of for
    fclose(file);
    return class_list;
}// end of create_class_list

int find(int idNo,student **list,int size)
{
    for(int i=0;i<size;i++)
    {
        if(idNo==list[i]->id)//checks each array if id inputted matches id in array
            return i;
    }//end of for

    return -1;//returns a -1 if idNo cannot be found
}// end of find

void input_grades(char *filename,student **list,int size)
{
    int stID;//hold the ID name
    FILE *file=fopen(filename,"r");
    for(int i=0;i<size;i++)
    {
        fscanf(file,"%d\n",&stID);
        fscanf(file,"%d%d\n",&(list[find(stID,list,size)]->pj1),&(list[find(stID,list,size)]->pj2));
        //uses the find function to find the index of the student id from the file and place the grades in corresponding student location
    }// end of for
    fclose(file);
}// end of input_grades

void compute_final_course_grades(student **list,int size)
{
    float courseMark;//holds value for final grade

    for(int i=0;i<size;i++)
    {
        courseMark=(list[i]->pj1+list[i]->pj2)/2.0;//takes average
        list[i]->grade=courseMark;
    }// end of for

}// end of compute_final_course_grades

void output_final_course_grades(char *filename,student **list,int size)
{
    FILE *output=fopen(filename,"w");
    fprintf(output,"%d\n",size); //prints size in the beginning of the file

    for(int i=0;i<size;i++)
    {
        fprintf(output,"%d %s %s %.2f\n",list[i]->id,list[i]->firstName,list[i]->surName,list[i]->grade);
        //loop to print out each students id and final grade
    }// end of for

    fclose(output);

}//end of output_final_course_grades

void print_list(student **list,int size)
{
    for(int i=0;i<size;i++)
    {
        printf("ID:%d | Name:%s %s | Project 1 Grade:%d | Project 2 Grade:%d | Final Grade: %.2f\n",list[i]->id,list[i]->firstName,list[i]->surName,list[i]->pj1,list[i]->pj2,list[i]->grade);
        //prints onto screen
    }//end of for
}// end of print_list

void withdraw(int idNo,student **list,int *sizePtr)
{
    if(find(idNo,list,*sizePtr)!=-1)//checks if id exits
    {
        int index=find(idNo,list,*sizePtr);//find to which index the id belongs to

       for(int i=index;i<*sizePtr;i++)
        {
            list[i]=list[i+1];//assigns the next student in the list the previous spot
        }// end of for

        *sizePtr-=1;//decrease the sizePtr by 1
        free(list[*sizePtr]); //deallocate memory of the last student

        printf("\nStudent removed.\n\n");
    }
    else
    {
        printf("\nStudent cannot be found.\n\n");
    }// end of if else
}//end of withdraw

void destroy_list(student **list,int *sizePtr)
{
    for(int i=0;i<*sizePtr;i++)
    {
        free(list[i]);//free each element of list
    }// end of for

    free(list);//free the whole list
    *sizePtr=0;
}// end of destroy_list










