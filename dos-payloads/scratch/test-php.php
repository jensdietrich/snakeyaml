<?php

$before = memory_get_usage();

function convert($size)
{
    $unit=array('b','kb','mb','gb','tb','pb');
    return @round($size/pow(1024,($i=floor(log($size,1024)))),2).' '.$unit[$i];
}

$o = yaml_parse_file("q.yaml");

$after = memory_get_usage();

echo convert($after - $before);


/*
foreach($o as $key => $item) {
 foreach($item as $k => $i) {
   //debug_zval_dump($k);
   // echo $k."\n";
  }
}
*/

?>