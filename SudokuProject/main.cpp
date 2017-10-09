#include<iostream>
#include<ctime>
#include<fstream>
using namespace std;
int num[9][9];
int b[9],a[9];
bool check(int i,int j,int k)
{
	int m,n;
	//判断行是否相同
	for(n=0;n<9;n++) 
	{
		if(num[i][n] == k)
			return false;
	}
	//判断列是否相同
	for(m=0;m<9;m++)
	{
		if(num[m][j]==k)
		return false;
	}
    //判断块是否相同
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
	//如果大于第九行，则结束
	if (i > 8) {
		return true;
	}
	//符合check函数条件，则填入
	if (check(i, j, *a)){
		num[i][j] = *a;
		if (sudoku(i + (j + 1) / 9, (j + 1) % 9, b)) {//从i行的元素一个一个依次递归，填入相应符合条件的数字
			return true;
		}
	}
	num[i][j] = 0;
	//判断b中的元素是否已经用完
	if (a - b >= 8) {
		return false;
	}
	//若没有合适的数字可以填入，则递归下一个准备填入的数字是否符合
	if (sudoku(i, j, a + 1)) {
		return true;
	}	
}

int main(){
	int i,j,index,n,z;
	 ofstream location_out;
    location_out.open("sudoku.txt", std::ios::out | std::ios::app); 
    srand(unsigned(time(0)));
	cin>>n;
	for(z=0;z<n;z++){
		for(i=0;i<9;i++){
				a[i]=i+1;
		}     //生成一个数组存放1-9
		for(i=0;i<9;){
			index=rand()%9;
			if(a[index]!=0){
			num[0][i]=a[index];   //生成随机数下标，并赋值给num，b,使得num，b存入1-9互不相同的随机数
			b[i]=a[index];
			a[index]=0;
			++i;
			}
		}
	
		sudoku(1,0,b);
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++){
			  cout<<num[i][j]<<" ";
			  location_out<<num[i][j]<<" ";
			}
			cout<<endl;
			location_out<<endl;
		 
		}
		cout<<endl;
		location_out<<endl;
		for(i=0;i<9;i++){
			for(j=0;j<9;j++){
			num[i][j]=0;
		}
		}
	}
	return 0;
}