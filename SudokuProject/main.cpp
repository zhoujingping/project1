#include<iostream>
#include<ctime>
#include<fstream>
#include<stdlib.h>

using namespace std;
int num[9][9];
int b[9],a[9],c[9];
bool check(int i,int j,int k)
{
	int m,n;
	//�ж����Ƿ���ͬ
	for(n=0;n<9;n++) 
	{
		if(num[i][n] == k)
			return false;
	}
	//�ж����Ƿ���ͬ
	for(m=0;m<9;m++)
	{
		if(num[m][j]==k)
		return false;
	}
    //�жϿ��Ƿ���ͬ
	int t1=(i/3)*3,t2=(j/3)*3;
	for(m=t1;m<t1+3;m++)
	{
		for(n=t2;n<t2+3;n++)
		{
			if(num[m][n] == k)
				return false;
		}
	}
	return true;
}
bool sudoku(int i, int j, int *a) {
	//������ڵھ��У������
	if (i > 8) {
		return true;
	}
	//����check����������������
	if (check(i, j, *a)){
		num[i][j] = *a;
		if (sudoku(i + (j + 1) / 9, (j + 1) % 9, b)) {//��i�е�Ԫ��һ��һ�����εݹ飬������Ӧ��������������
			return true;
		}
	}
	num[i][j] = 0;
	//�ж�b�е�Ԫ���Ƿ��Ѿ�����
	if (a - b >= 8) {
		return false;
	}
	//��û�к��ʵ����ֿ������룬��ݹ���һ��׼������������Ƿ����
	if (sudoku(i, j, a + 1)) {
		return true;
	}	
}
int getTimes(int argc,char *argv[]){
	int times;
	if(argc==3&&strcmp(argv[1],"-c")==0){
		times = atoi(argv[2]);
		return times;
	}else{
		cout<<"Input format: sudoku [ -c parameter ]\n";
		return -1;
	}
}

int main(int argc,char *argv[]){
	int i,j,index,n,z;
	 ofstream location_out;
    location_out.open("sudoku.txt"); 
    srand(unsigned(time(0)));
	n = getTimes(argc,argv);
	for(z=0;z<n;z++){
		num[0][0]=(1+2)%9+1;
		for(i=0;i<9;i++){
				a[i]=i+1;
		}     //����һ��������1-9
		for(i=0;i<9;i++){
				c[i]=i+1;
		}     
		for(i=0;i<9;){
			index=rand()%9;
			if(c[index]!=0){   //����������±꣬����ֵ��num��b,ʹ��num��b����1-9������ͬ�������
			b[i]=c[index];
			c[index]=0;
			++i;
			}
		}     
		for(i=1;i<9;){
			index=rand()%9;
			if(a[index]!=0 && a[index]!=4){
			num[0][i]=a[index];   //����������±꣬����ֵ��num��b,ʹ��num��b����1-9������ͬ�������
			a[index]=0;
			++i;
			}
		}
		sudoku(1,0,b);
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++){
			  location_out<<num[i][j]<<" ";
			}
				location_out<<endl;
		}
		location_out<<endl;
		for(i=0;i<9;i++){
			for(j=0;j<9;j++){
			num[i][j]=0;
		}
		}
	}
	location_out.close();
	return 0;
}